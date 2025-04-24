package fr.eni.encheres.service.articleEnchere;


import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.EtatVente;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.article.ArticleRepository;
import fr.eni.encheres.service.enchere.EnchereRepository;
import fr.eni.encheres.service.implementation.ArticleServiceImpl;
import fr.eni.encheres.service.implementation.UtilisateurServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Profile("Prod")
@Repository
public class ArticleEnchereServiceJPA implements ArticleEnchereService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EnchereRepository enchereRepository;

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Autowired
    private ArticleServiceImpl articleService;

    @Override
    public List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres() {
        verifierEtFinaliserEncheres();

        // Utiliser le repository pour obtenir les articles en cours
        List<ArticleVendu> articlesEnVente = articleRepository.findByEtatVente(EtatVente.EN_COURS);
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();

        for (ArticleVendu article : articlesEnVente) {
            // Utiliser le repository pour trouver l'enchère maximale
            Enchere bestEnchere = enchereRepository.findMaxEnchereByNoArticle(article.getNoArticle());
            result.add(new ArticleWithBestEnchereDTO(article, bestEnchere));
        }
        return result;
    }

    @Override
    @Transactional
    public void addEnchere(String userName, Long noArticle, int propal) {
        // Récupérer l'utilisateur et l'article
        Optional<Utilisateur> optUser = utilisateurService.getUtilisateurByPseudo(userName).data;
        if (optUser.isEmpty()) {
            return;
        }

        Utilisateur user = optUser.get();
        ArticleVendu article = articleService.getArticleById(noArticle).data;

        // Vérification article en cours de vente
        if (article == null || !article.getEtatVente().equals(EtatVente.EN_COURS)) {
            return;
        }

        // Vérifier les conditions préalables
        if (article.getVendeur().equals(user)) {
            return; // Un vendeur ne peut pas enchérir sur son propre article
        }

        // Trouver l'enchère maximale actuelle
        Enchere maxEnchere = enchereRepository.findMaxEnchereByNoArticle(noArticle);

        // Vérifier si l'enchère est valide
        int enchereMinimale = maxEnchere != null ? maxEnchere.getMontantEnchere() : article.getMisAPrix();

        // Si l'enchère n'est pas supérieure à l'enchère maximale actuelle ou au prix initial
        if (propal <= enchereMinimale) {
            return;
        }

        // Vérifier si l'utilisateur est déjà le meilleur enchérisseur
        boolean isCurrentBestBidder = maxEnchere != null &&
                maxEnchere.getEncherisseur().getId().equals(user.getId());

        // Calculer le montant à débiter
        int montantADebiter = isCurrentBestBidder ?
                propal - maxEnchere.getMontantEnchere() :
                propal;

        // Vérifier si l'utilisateur a suffisamment de crédits
        if (user.getCredit() < montantADebiter) {
            return;
        }

        // Re-créditer l'enchérisseur précédent si ce n'est pas le même utilisateur
        if (maxEnchere != null && !isCurrentBestBidder) {
            Utilisateur ancienEncherisseur = maxEnchere.getEncherisseur();
            int montantAncienneEnchere = maxEnchere.getMontantEnchere();
            utilisateurService.addCredits(ancienEncherisseur, montantAncienneEnchere);
        }

        // Débiter l'utilisateur du montant approprié
        utilisateurService.removeCredits(user, montantADebiter);

        if (isCurrentBestBidder) {
            // Mettre à jour l'enchère existante du même utilisateur
            maxEnchere.setDateEnchere(LocalDateTime.now());
            maxEnchere.setMontantEnchere(propal);
            enchereRepository.save(maxEnchere);
        } else {
            // Créer une nouvelle enchère pour un nouvel enchérisseur
            Enchere nouvelleEnchere = new Enchere();
            nouvelleEnchere.setDateEnchere(LocalDateTime.now());
            nouvelleEnchere.setMontantEnchere(propal);
            nouvelleEnchere.setArticleVendu(article);
            nouvelleEnchere.setEncherisseur(user);
            enchereRepository.save(nouvelleEnchere);
        }
    }

    @Override
    public ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle) {
        verifierEtFinaliserEncheres();

        // Récupérer directement l'article par son ID
        ArticleVendu article = articleRepository.findById(noArticle).orElse(null);
        if (article == null) {
            return null;
        }

        // Récupérer la meilleure enchère avec le repository
        Enchere meilleureEnchere = enchereRepository.findMaxEnchereByNoArticle(noArticle);
        return new ArticleWithBestEnchereDTO(article, meilleureEnchere);
    }

    @Override
    @Transactional
    public void verifierEtFinaliserEncheres() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Mettre à jour les articles CREEE dont la date de début est passée
        List<ArticleVendu> articlesToStart = articleRepository.findByEtatVenteAndDateDebutEncheresLessThan(
                EtatVente.CREEE, now);

        for (ArticleVendu article : articlesToStart) {
            article.setEtatVente(EtatVente.EN_COURS);
            articleRepository.save(article);
        }

        // 2. Finaliser les articles EN_COURS dont la date de fin est passée
        List<ArticleVendu> articlesToFinalize = articleRepository.findArticlesToFinalize(now);

        for (ArticleVendu article : articlesToFinalize) {
            article.setEtatVente(EtatVente.TERMINEE);
            articleRepository.save(article);

            finaliserEnchere(article.getNoArticle());
        }
    }

    @Override
    @Transactional
    public ArticleWithBestEnchereDTO finaliserEnchere(Long noArticle) {
        // Récupérer l'article
        ArticleVendu article = articleRepository.findById(noArticle).orElse(null);

        if (article == null || article.getEtatVente() != EtatVente.TERMINEE) {
            return getArticleWithBestEnchere(noArticle);
        }

        // Récupérer la meilleure enchère
        Enchere meilleureEnchere = enchereRepository.findMaxEnchereByNoArticle(noArticle);

        if (meilleureEnchere != null) {
            // Article vendu
            article.setEtatVente(EtatVente.VENDU);
            article.setPrixVente(meilleureEnchere.getMontantEnchere());

            // Récupérer l'acheteur
            Utilisateur acheteur = meilleureEnchere.getEncherisseur();

            // Définir l'acheteur
            article.setAcheteur(acheteur);

            // Ajouter l'article à la liste des achats de l'utilisateur
            if (!acheteur.getArticleAchatList().contains(article)) {
                acheteur.getArticleAchatList().add(article);
            }

            // Sauvegarder les modifications
            articleRepository.save(article);
        } else {
            // Aucune enchère
            article.setEtatVente(EtatVente.NON_VENDU);
            articleRepository.save(article);
        }

        // Retourner le DTO à jour
        return new ArticleWithBestEnchereDTO(article, meilleureEnchere);
    }

    @Override
    public List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName) {
        verifierEtFinaliserEncheres();

        List<ArticleVendu> articles;

        // Utiliser les méthodes de repository appropriées selon les critères
        if (noCategorie != null && searchName != null && !searchName.trim().isEmpty()) {
            // Recherche par catégorie ET nom
            articles = articleRepository.findByEtatVenteAndCategorieAndNomArticleContaining(
                    EtatVente.EN_COURS, noCategorie, searchName);
        } else if (noCategorie != null) {
            // Recherche par catégorie uniquement
            articles = articleRepository.findByEtatVenteAndCategorieNoCategorie(EtatVente.EN_COURS, noCategorie);
        } else if (searchName != null && !searchName.trim().isEmpty()) {
            // Recherche par nom uniquement
            articles = articleRepository.findByEtatVenteAndNomArticleContaining(EtatVente.EN_COURS, searchName);
        } else {
            // Aucun critère, retourner tous les articles en cours
            articles = articleRepository.findByEtatVente(EtatVente.EN_COURS);
        }

        // Transformer en DTO avec les meilleures enchères
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();
        for (ArticleVendu article : articles) {
            Enchere meilleureEnchere = enchereRepository.findMaxEnchereByNoArticle(article.getNoArticle());
            result.add(new ArticleWithBestEnchereDTO(article, meilleureEnchere));
        }
        return result;
    }

    @Override
    public List<ArticleWithBestEnchereDTO> advancedSearch(String username, SearchCriteriaDTO criteria) {
        verifierEtFinaliserEncheres();

        // Si l'utilisateur n'est pas connecté ou aucun mode n'est sélectionné
        if (criteria.getMode() == null || username == null || username.isEmpty()) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        // Récupérer l'utilisateur connecté
        Optional<Utilisateur> optUser = utilisateurService.getUtilisateurByPseudo(username).data;
        if (optUser.isEmpty()) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        Utilisateur user = optUser.get();
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();
        List<ArticleVendu> filteredArticles = new ArrayList<>();

        // Mode achats
        if ("achats".equals(criteria.getMode()) && criteria.getAchats() != null) {
            // Enchères ouvertes
            if (criteria.getAchats().contains("ouvertes")) {
                List<ArticleVendu> openArticles = articleRepository.findByVendeurIdNotAndEtatVente(
                        user.getId(), EtatVente.EN_COURS);
                filteredArticles.addAll(openArticles);
            }

            // Mes enchères en cours
            if (criteria.getAchats().contains("mesEncheres")) {
                // Récupérer les IDs des articles sur lesquels l'utilisateur a enchéri
                List<Long> articleIds = enchereRepository.findArticleIdsByEncherisseurId(user.getId());

                for (Long articleId : articleIds) {
                    ArticleVendu article = articleRepository.findById(articleId).orElse(null);
                    if (article != null && article.getEtatVente() == EtatVente.EN_COURS) {
                        filteredArticles.add(article);
                    }
                }
            }

            // Mes enchères remportées
            if (criteria.getAchats().contains("remportees")) {
                List<EtatVente> remporteesStates = Arrays.asList(
                        EtatVente.VENDU,
                        EtatVente.RETRAIT_EFFECTUE);

                List<ArticleVendu> wonArticles = articleRepository.findByAcheteurIdAndEtatVenteIn(
                        user.getId(), remporteesStates);
                filteredArticles.addAll(wonArticles);
            }
        }

        // Mode ventes
        else if ("ventes".equals(criteria.getMode()) && criteria.getVentes() != null) {
            // Mes ventes en cours
            if (criteria.getVentes().contains("enCours")) {
                List<ArticleVendu> ongoingSales = articleRepository.findByVendeurIdAndEtatVente(
                        user.getId(), EtatVente.EN_COURS);
                filteredArticles.addAll(ongoingSales);
            }

            // Ventes non débutées
            if (criteria.getVentes().contains("nonDebutees")) {
                List<ArticleVendu> notStartedSales = articleRepository.findByVendeurIdAndEtatVente(
                        user.getId(), EtatVente.CREEE);
                filteredArticles.addAll(notStartedSales);
            }

            // Ventes terminées
            if (criteria.getVentes().contains("terminees")) {
                List<EtatVente> finishedStates = Arrays.asList(
                        EtatVente.TERMINEE,
                        EtatVente.VENDU,
                        EtatVente.NON_VENDU,
                        EtatVente.RETRAIT_EFFECTUE);

                List<ArticleVendu> finishedSales = articleRepository.findByVendeurIdAndEtatVenteIn(
                        user.getId(), finishedStates);
                filteredArticles.addAll(finishedSales);
            }
        }

        // Appliquer d'autres filtres (catégorie et nom)
        if (criteria.getNoCategorie() != null ||
                (criteria.getSearchName() != null && !criteria.getSearchName().trim().isEmpty())) {

            filteredArticles = filteredArticles.stream()
                    .filter(article -> {
                        boolean matchesCategory = criteria.getNoCategorie() == null ||
                                article.getCategorie().getNoCategorie().equals(criteria.getNoCategorie());

                        boolean matchesName = criteria.getSearchName() == null || criteria.getSearchName().trim().isEmpty() ||
                                article.getNomArticle().toLowerCase().contains(criteria.getSearchName().toLowerCase());

                        return matchesCategory && matchesName;
                    })
                    .distinct()
                    .toList();
        }

        // Si aucun article trouvé avec les filtres avancés ou aucune case cochée
        if (filteredArticles.isEmpty() && (criteria.getAchats() == null || criteria.getAchats().isEmpty())
                && (criteria.getVentes() == null || criteria.getVentes().isEmpty())) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        // Transformer en DTOs avec les meilleures enchères
        for (ArticleVendu article : filteredArticles) {
            Enchere meilleureEnchere = enchereRepository.findMaxEnchereByNoArticle(article.getNoArticle());
            result.add(new ArticleWithBestEnchereDTO(article, meilleureEnchere));
        }

        return result;
    }
}

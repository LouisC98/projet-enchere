package fr.eni.encheres.service.articleEnchere;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.EtatVente;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.user.UtilisateurService;
import fr.eni.encheres.service.implementation.ArticleServiceImpl;
import fr.eni.encheres.service.implementation.EnchereServiceImpl;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArticleEnchereServiceMock implements ArticleEnchereService {

    @Autowired
    private ArticleServiceImpl articleService;
    @Autowired
    private EnchereServiceImpl enchereServiceImpl;
    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres() {
        verifierEtFinaliserEncheres();

        List<ArticleVendu> articles = articleService.getAllArticles().data;
        List<ArticleVendu> articlesEnVente = articles.stream().filter(article -> article.getEtatVente().equals(EtatVente.EN_COURS)).toList();
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();
        
        for (ArticleVendu article : articlesEnVente) {
            Enchere bestEnchere = enchereServiceImpl.getMaxEnchere(article.getNoArticle()).data;
            result.add(new ArticleWithBestEnchereDTO(article, bestEnchere));
        }
        return result;
    }

    @Override
    public void addEnchere(String userName, Long noArticle, int propal) {
        // Récupérer l'utilisateur et l'article
        Utilisateur user = utilisateurService.getUtilisateurByPseudo(userName).orElseThrow();
        ArticleVendu article = articleService.getArticleById(noArticle).data;

        // Verification article en cours de vente
        if (!article.getEtatVente().equals(EtatVente.EN_COURS)) {
            return;
        }

        // Vérifier les conditions préalables
        if (article.getVendeur().equals(user)) {
            return; // Un vendeur ne peut pas enchérir sur son propre article
        }

        // Trouver l'enchère maximale actuelle
        Enchere maxEnchere = enchereServiceImpl.getMaxEnchere(noArticle).data;

        // Vérifier si l'enchère est valide
        int enchereMinimale = maxEnchere != null ? maxEnchere.getMontantEnchere() : article.getMisAPrix();

        // Si l'enchère n'est pas supérieure à l'enchère maximale actuelle ou au prix initial
        if (propal <= enchereMinimale) {
            return;
        }

        // Vérifier si l'utilisateur est déjà le meilleur enchérisseur
        boolean isCurrentBestBidder = maxEnchere != null &&
                maxEnchere.getEncherisseur().getId().equals(user.getId());


        // Calculer le montant à débiter (ajustement si l'utilisateur surenchérit sur sa propre enchère)
        int montantADebiter = isCurrentBestBidder ?
                propal - maxEnchere.getMontantEnchere() :
                propal;

        // Vérifier si l'utilisateur a suffisamment de crédits pour cette nouvelle enchère
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
            enchereServiceImpl.updateEnchere(maxEnchere);
        } else {
            // Créer une nouvelle enchère pour un nouvel enchérisseur
            Enchere nouvelleEnchere = new Enchere();
            nouvelleEnchere.setDateEnchere(LocalDateTime.now());
            nouvelleEnchere.setMontantEnchere(propal);
            nouvelleEnchere.setArticleVendu(article);
            nouvelleEnchere.setEncherisseur(user);
            enchereServiceImpl.addEnchere(nouvelleEnchere);
        }
    }

    @Override
    public void verifierEtFinaliserEncheres() {
        List<ArticleVendu> articles = articleService.getAllArticles().data;

        for (ArticleVendu article : articles) {
            // Utiliser la méthode qui retourne un ServiceResponse
            ServiceResponse<ArticleVendu> response = articleService.updateEtatVente(article.getNoArticle());

        if (response.code.equals(ServiceConstant.CD_SUCCESS) && response.data != null) {
                ArticleVendu updatedArticle = response.data;

                // Si l'article est en état TERMINEE, le finaliser
                if (updatedArticle.getEtatVente() == EtatVente.TERMINEE) {
                    finaliserEnchere(updatedArticle.getNoArticle());
                }
            }
        }
    }

    @Override
    public ArticleWithBestEnchereDTO finaliserEnchere(Long noArticle) {
        // Récupérer l'article
        ArticleVendu article = articleService.getArticleById(noArticle).data;

        if (article == null || article.getEtatVente() != EtatVente.TERMINEE) {
            // Si l'article n'existe pas ou n'est pas terminé, retourner simplement le DTO
            return getArticleWithBestEnchere(noArticle);
        }

        // Récupérer la meilleure enchère
        Enchere meilleureEnchere = enchereServiceImpl.getMaxEnchere(noArticle).data;

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
        } else {
            // Aucune enchère
            article.setEtatVente(EtatVente.NON_VENDU);
        }

        // Pas besoin de mise à jour explicite car les objets Java sont modifiés par référence

        // Retourner le DTO à jour
        return new ArticleWithBestEnchereDTO(article, meilleureEnchere);
    }


    public void mockEnchere() {
        List<ArticleVendu> articles = articleService.getAllArticles().data;
        if (articles.size() >= 2) {
            Enchere enchere1 = new Enchere();
            Enchere enchere2 = new Enchere();
            enchere1.setMontantEnchere(articles.get(0).getMisAPrix());
            enchere2.setMontantEnchere(articles.get(1).getMisAPrix());
            enchere1.setArticleVendu(articles.get(0));
            enchere2.setArticleVendu(articles.get(1));
            enchere1.setDateEnchere(LocalDateTime.now());
            enchere2.setDateEnchere(LocalDateTime.now());
            enchere1.setEncherisseur(utilisateurService.getUtilisateurById(1).orElseThrow());
            enchere2.setEncherisseur(utilisateurService.getUtilisateurById(2).orElseThrow());
            System.out.println(articles.get(1).getMisAPrix());
            enchereServiceImpl.addEnchere(enchere1);
            enchereServiceImpl.addEnchere(enchere2);
        }
    }

    @Override
    public List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName) {
        List<ArticleVendu> articles = articleService.getAllArticles().data;
        verifierEtFinaliserEncheres();

        articles = articles.stream()
                .filter(article -> article.getEtatVente() == EtatVente.EN_COURS)
                .collect(Collectors.toList());

        // Filtrer par catégorie
        if (noCategorie != null) {
            articles = articles.stream()
                    .filter(article -> article.getCategorie().getNoCategorie().equals(noCategorie))
                    .collect(Collectors.toList());
        }

        // Filtrer par nom
        if (searchName != null && !searchName.trim().isEmpty()) {
            String searchNameLower = searchName.toLowerCase();
            articles = articles.stream()
                    .filter(article -> article.getNomArticle().toLowerCase().contains(searchNameLower))
                    .collect(Collectors.toList());
        }

        // Transformer en DTO avec les meilleures enchères
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();
        for (ArticleVendu article : articles) {
            Enchere meilleureEnchere = enchereServiceImpl.getMaxEnchere(article.getNoArticle()).data;
            result.add(new ArticleWithBestEnchereDTO(article, meilleureEnchere));
        }
        return result;
    }

    @Override
    public ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle) {
        verifierEtFinaliserEncheres();

        ArticleVendu article = articleService.getArticleById(noArticle).data;
        Enchere meilleureEnchere = enchereServiceImpl.getMaxEnchere(noArticle).data;

        return new ArticleWithBestEnchereDTO(article, meilleureEnchere);
    }


    @Override
    public List<ArticleWithBestEnchereDTO> advancedSearch(String username, SearchCriteriaDTO criteria) {
        verifierEtFinaliserEncheres();

        // Si l'utilisateur n'est pas connecté ou aucun mode n'est sélectionné,
        // utiliser la recherche simple par catégorie et nom
        if (criteria.getMode() == null || username == null || username.isEmpty()) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        // Récupérer l'utilisateur connecté
        Optional<Utilisateur> currentUser = utilisateurService.getUtilisateurByPseudo(username);
        if (currentUser.isEmpty()) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        List<ArticleVendu> articles = articleService.getAllArticles().data;
        List<ArticleVendu> filteredArticles = new ArrayList<>();

        // Filtrer d'abord par catégorie et nom d'article (filtres de base)
        if (criteria.getNoCategorie() != null) {
            articles = articles.stream()
                    .filter(article -> article.getCategorie().getNoCategorie().equals(criteria.getNoCategorie()))
                    .collect(Collectors.toList());
        }

        if (criteria.getSearchName() != null && !criteria.getSearchName().trim().isEmpty()) {
            String searchNameLower = criteria.getSearchName().toLowerCase();
            articles = articles.stream()
                    .filter(article -> article.getNomArticle().toLowerCase().contains(searchNameLower))
                    .collect(Collectors.toList());
        }

        Utilisateur user = currentUser.get();

        // Mode achats
        if ("achats".equals(criteria.getMode()) && criteria.getAchats() != null) {

            // Enchères ouvertes
            if (criteria.getAchats().contains("ouvertes")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> !article.getVendeur().equals(user)) // Pas ses propres articles
                                .filter(article -> article.getEtatVente().equals(EtatVente.EN_COURS))
                                .toList()
                );
            }

            // Mes enchères en cours
            if (criteria.getAchats().contains("mesEncheres")) {
                // Récupérer les articles sur lesquels l'utilisateur a enchéri
                List<Long> userEnchereArticleIds = enchereServiceImpl.getAllEncheres().data.stream()
                        .filter(enchere -> enchere.getEncherisseur().equals(user))
                        .map(enchere -> enchere.getArticleVendu().getNoArticle())
                        .distinct()
                        .toList();

                // Filtrer les articles correspondants qui sont toujours en cours
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> userEnchereArticleIds.contains(article.getNoArticle()))
                                .filter(article -> article.getEtatVente().equals(EtatVente.EN_COURS))
                                .toList()
                );
            }

            // Mes enchères remportées
            if (criteria.getAchats().contains("remportees")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getEtatVente().equals(EtatVente.VENDU))
                                .filter(article -> article.getAcheteur() != null && article.getAcheteur().equals(user))
                                .toList()
                );
            }
        }

        // Mode ventes
        else if ("ventes".equals(criteria.getMode()) && criteria.getVentes() != null) {

            // Mes ventes en cours
            if (criteria.getVentes().contains("enCours")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getEtatVente().equals(EtatVente.EN_COURS))
                                .toList()
                );
            }

            // Ventes non débutées
            if (criteria.getVentes().contains("nonDebutees")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getEtatVente().equals(EtatVente.CREEE))
                                .toList()
                );
            }

            // Ventes terminées
            if (criteria.getVentes().contains("terminees")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getEtatVente().equals(EtatVente.TERMINEE) ||
                                        article.getEtatVente().equals(EtatVente.VENDU) ||
                                        article.getEtatVente().equals(EtatVente.NON_VENDU) ||
                                        article.getEtatVente().equals(EtatVente.RETRAIT_EFFECTUE))
                                .toList()
                );
            }
        }

        // Si aucun article trouvé avec les filtres avancés ou aucune case cochée, revenir à la recherche simple
        if (filteredArticles.isEmpty() && (criteria.getAchats() == null || criteria.getAchats().isEmpty())
                && (criteria.getVentes() == null || criteria.getVentes().isEmpty())) {
            return searchArticles(criteria.getNoCategorie(), criteria.getSearchName());
        }

        // Éliminer les doublons éventuels
        filteredArticles = filteredArticles.stream().distinct().collect(Collectors.toList());

        // Transformer en DTO avec les meilleures enchères
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();
        for (ArticleVendu article : filteredArticles) {
            Enchere meilleureEnchere = enchereServiceImpl.getMaxEnchere(article.getNoArticle()).data;
            result.add(new ArticleWithBestEnchereDTO(article, meilleureEnchere));
        }
        return result;
    }
}

package fr.eni.encheres.service.articleEnchere;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.user.UtilisateurService;
import fr.eni.encheres.service.implementation.ArticleServiceImpl;
import fr.eni.encheres.service.implementation.EnchereServiceImpl;
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
        List<ArticleVendu> articles = articleService.getAllArticles().data;
        List<ArticleWithBestEnchereDTO> result = new ArrayList<>();

        for (ArticleVendu article : articles) {
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

        // Vérifier les conditions préalables
        if (article.getVendeur().equals(user) || user.getCredit() < propal) {
            return;
        }

        // Trouver l'enchère maximale actuelle
        Optional<Enchere> maxEnchere = enchereServiceImpl.getEnchereByArticle(noArticle).data.stream()
                .max(Comparator.comparingInt(Enchere::getMontantEnchere));

        // Vérifier si l'enchère est valide
        int enchereMinimale = maxEnchere.map(Enchere::getMontantEnchere).orElseGet(article::getMisAPrix);

        // Si l'enchère n'est pas supérieure à l'enchère maximale actuelle ou au prix initial
        if (propal <= enchereMinimale) {
            return;
        }

        // Re-créditer l'enchérisseur précédent si il existe
        maxEnchere.ifPresent(enchere -> {
            Utilisateur ancienEncherisseur = enchere.getEncherisseur();
            int montantAncienneEnchere = enchere.getMontantEnchere();
            utilisateurService.addCredits(ancienEncherisseur, montantAncienneEnchere);
        });

        // Créer et enregistrer la nouvelle enchère
        Enchere nouvelleEnchere = new Enchere();
        nouvelleEnchere.setDateEnchere(LocalDateTime.now());
        nouvelleEnchere.setMontantEnchere(propal);
        nouvelleEnchere.setArticleVendu(article);
        nouvelleEnchere.setEncherisseur(user);

        utilisateurService.removeCredits(user, propal);
        enchereServiceImpl.addEnchere(nouvelleEnchere);
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
        ArticleVendu article = articleService.getArticleById(noArticle).data;
        Enchere meilleureEnchere = enchereServiceImpl.getMaxEnchere(noArticle).data;
        return new ArticleWithBestEnchereDTO(article, meilleureEnchere);
    }


    @Override
    public List<ArticleWithBestEnchereDTO> advancedSearch(String username, SearchCriteriaDTO criteria) {
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
        LocalDateTime now = LocalDateTime.now();

        // Mode achats
        if ("achats".equals(criteria.getMode()) && criteria.getAchats() != null) {

            // Enchères ouvertes
            if (criteria.getAchats().contains("ouvertes")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> !article.getVendeur().equals(user)) // Pas ses propres articles
                                .filter(article -> article.getDateDebutEncheres().isBefore(now) &&
                                        article.getDateFinEnchere().isAfter(now))
                                .collect(Collectors.toList())
                );
            }

            // Mes enchères en cours
            if (criteria.getAchats().contains("mesEncheres")) {
                // Récupérer les articles sur lesquels l'utilisateur a enchéri
                List<Long> userEnchereArticleIds = enchereServiceImpl.getAllEncheres().data.stream()
                        .filter(enchere -> enchere.getEncherisseur().equals(user))
                        .map(enchere -> enchere.getArticleVendu().getNoArticle())
                        .distinct()
                        .collect(Collectors.toList());

                // Filtrer les articles correspondants qui sont toujours en cours
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> userEnchereArticleIds.contains(article.getNoArticle()))
                                .filter(article -> article.getDateFinEnchere().isAfter(now))
                                .collect(Collectors.toList())
                );
            }

            // Mes enchères remportées
            if (criteria.getAchats().contains("remportees")) {
                // Articles dont l'enchère est terminée
                List<ArticleVendu> terminatedArticles = articles.stream()
                        .filter(article -> article.getDateFinEnchere().isBefore(now))
                        .collect(Collectors.toList());

                // Pour chaque article terminé, vérifier si l'utilisateur est le meilleur enchérisseur
                for (ArticleVendu article : terminatedArticles) {
                    Enchere bestEnchere = enchereServiceImpl.getMaxEnchere(article.getNoArticle()).data;
                    if (bestEnchere != null && bestEnchere.getEncherisseur().equals(user)) {
                        filteredArticles.add(article);
                    }
                }
            }
        }

        // Mode ventes
        else if ("ventes".equals(criteria.getMode()) && criteria.getVentes() != null) {

            // Mes ventes en cours
            if (criteria.getVentes().contains("enCours")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getDateDebutEncheres().isBefore(now) &&
                                        article.getDateFinEnchere().isAfter(now))
                                .collect(Collectors.toList())
                );
            }

            // Ventes non débutées
            if (criteria.getVentes().contains("nonDebutees")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getDateDebutEncheres().isAfter(now))
                                .collect(Collectors.toList())
                );
            }

            // Ventes terminées
            if (criteria.getVentes().contains("terminees")) {
                filteredArticles.addAll(
                        articles.stream()
                                .filter(article -> article.getVendeur().equals(user))
                                .filter(article -> article.getDateFinEnchere().isBefore(now))
                                .collect(Collectors.toList())
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

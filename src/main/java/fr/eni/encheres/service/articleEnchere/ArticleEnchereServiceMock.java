package fr.eni.encheres.service.articleEnchere;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.service.UtilisateurService;
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
}

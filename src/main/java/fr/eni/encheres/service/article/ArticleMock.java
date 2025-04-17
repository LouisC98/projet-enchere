package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.service.implementation.CategorieServiceImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ArticleMock implements ArticleServiceInterface {

    private List<ArticleVendu> articles = new ArrayList<>();
    private static int INDEX = 2;

    CategorieServiceImpl categorieService;

    public ArticleMock(CategorieServiceImpl categorieService ) {
        this.categorieService = categorieService;
        mockArticles();
    }

    @Override
    public void creerArticle(ArticleVendu article) {
        INDEX++;
        article.setNoArticle((long) INDEX);
        article.setCategorie(categorieService.getCategorie(article.getCategorie().getNoCategorie()));
        article.getRetrait().setArticleVendu(Optional.of(article));
        articles.add(article);
    }

    @Override
    public List<ArticleVendu> getArticles() {
        return articles;
    }

    @Override
    public ArticleVendu getArticle(Long noArticle) {
        return articles.stream()
                .filter(article -> article.getNoArticle().equals(noArticle))
                .findFirst()
                .orElse(null);
    }

    public List<ArticleVendu> searchArticles(Long noCategorie, String searchName) {
        List<ArticleVendu> result = new ArrayList<>(getArticles());

        if (noCategorie != null) {
            result = result.stream()
                    .filter(article -> article.getCategorie().getNoCategorie().equals(noCategorie))
                    .collect(Collectors.toList());
        }

        if (searchName != null && !searchName.trim().isEmpty()) {
            String searchNameLower = searchName.toLowerCase();
            result = result.stream()
                    .filter(article -> article.getNomArticle().toLowerCase().contains(searchNameLower))
                    .collect(Collectors.toList());
        }

        return result;
    }

    /**
     * Mock des articles
     * Ajouter les adresses par défaut de l'acheteur.
     */
    public void mockArticles(){
        ArticleVendu fauteuil = new ArticleVendu(
                123L,
                "Fauteuil",
                "Fauteil en cuir",
                categorieService.getCategories().get(0),
                310,
                LocalDateTime.of(2018, 8, 10, 14, 45),
                LocalDateTime.of(2022, 12, 1, 18, 15),
                new Retrait("test",48, "Niort"),
                new Enchere(LocalDateTime.now(),8));




        ArticleVendu pc = new ArticleVendu(
                2L,
                "PC Gamer",
                "Un PC Gamer haute performance avec une carte graphique de dernière génération",
                categorieService.getCategories().get(1),
                1000,
                LocalDateTime.of(2025, 4, 15, 10, 30),
                LocalDateTime.of(2030, 6, 5, 7, 0),
                new Retrait("test 18 rue", 87, "La Rochelle"),
                new Enchere(LocalDateTime.now(),28));

        articles.add(fauteuil);
        articles.add(pc);
    }
}

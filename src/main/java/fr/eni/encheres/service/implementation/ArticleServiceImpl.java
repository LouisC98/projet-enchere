package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private List<ArticleVendu> articles = new ArrayList<>();

    CategorieServiceImpl categorieService;


    public ArticleServiceImpl(CategorieServiceImpl categorieService ) {
        this.categorieService = categorieService;
        mockArticles();
    }

    @Override
    public void creerArticle(ArticleVendu article) {
        articles.add(article);
    }

    @Override
    public List<ArticleVendu> getArticles() {
        return articles;
    }

    /**
     * Mock des articles
     * Ajouter les adresses par défaut de l'acheteur.
     */
    public void mockArticles(){
        ArticleVendu fauteuil = new ArticleVendu(
                "123",
                "Fauteil",
                "Fauteil en cuir",
                categorieService.getCategories().get(0),
                310,
                LocalDateTime.of(2018, 8, 10, 14, 45),
                LocalDateTime.of(2022, 12, 1, 18, 15),
                new Retrait("test",48, "Niort")

        );


        ArticleVendu pc = new ArticleVendu(
                "002",
                "PC Gamer",
                "Un PC Gamer haute performance avec une carte graphique de dernière génération",
                categorieService.getCategories().get(1),
                1000,
                LocalDateTime.of(2025, 4, 15, 10, 30),
                LocalDateTime.of(2030, 6, 5, 7, 0),
                new Retrait("test 18 rue", 87, "La Rochelle")
        );


        articles.add(pc);
        articles.add(fauteuil);
    }
}

package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.ArticleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private List<ArticleVendu> articles = new ArrayList<>();

    public ArticleServiceImpl() {
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

    public void mockArticles(){
        ArticleVendu fauteuil = new ArticleVendu(
                "123",
                "Fauteil",
                "Fauteil en cuir",
                LocalDateTime.of(2018, 8, 10, 14, 45),
                LocalDateTime.of(2022, 12, 1, 18, 15),
                310,
                400,
                "En cours"
        );

        ArticleVendu pc = new ArticleVendu(
                "002",
                "PC Gamer",
                "Un PC Gamer haute performance avec une carte graphique de dernière génération",
                LocalDateTime.of(2025, 4, 15, 10, 30),
                LocalDateTime.of(2030, 6, 5, 7, 0),
                1000,
                1444,
                "Vendu"
        );

        articles.add(pc);
        articles.add(fauteuil);
    }
}

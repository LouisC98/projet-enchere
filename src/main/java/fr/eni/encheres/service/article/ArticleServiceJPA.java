package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class ArticleServiceJPA implements ArticleService {

    private ArticleRepository articleRepository;

    @Override
    public void creerArticle(ArticleVendu article, String username) {

    }

    @Override
    public List<ArticleVendu> getArticles() {
        return List.of();
    }

    @Override
    public ArticleVendu getArticle(Long noArticle) {
        return null;
    }

    @Override
    public ArticleVendu updateEtatVente(ArticleVendu article) {
        return null;
    }
}

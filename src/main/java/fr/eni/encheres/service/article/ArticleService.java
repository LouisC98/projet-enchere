package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;

import java.util.List;

public interface ArticleService {

    public void creerArticle(ArticleVendu article, String username);

    public List<ArticleVendu> getArticles();

    ArticleVendu getArticle(Long noArticle);

    ArticleVendu updateEtatVente(ArticleVendu article);

    void validerRetrait(Long noArticle, Utilisateur utilisateur);
}

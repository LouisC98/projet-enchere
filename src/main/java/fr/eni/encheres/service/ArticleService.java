package fr.eni.encheres.service;

import fr.eni.encheres.bo.ArticleVendu;

import java.util.List;

public interface ArticleService {

    public void creerArticle(ArticleVendu article, String username);

    public List<ArticleVendu> getArticles();

    ArticleVendu getArticle(Long noArticle);
}

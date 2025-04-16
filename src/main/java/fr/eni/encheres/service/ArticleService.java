package fr.eni.encheres.service;

import fr.eni.encheres.bo.ArticleVendu;

import java.util.List;

public interface ArticleService {

    public void creerArticle(ArticleVendu article);

    public List<ArticleVendu> getArticles();

    ArticleVendu getArticle(Long noArticle);

    List<ArticleVendu> searchArticles(Long noCategorie, String searchName);
}

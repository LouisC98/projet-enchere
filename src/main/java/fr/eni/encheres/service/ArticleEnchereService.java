package fr.eni.encheres.service;

import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;

import java.util.List;

public interface ArticleEnchereService {

    List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres();
    void addEnchere(String userName, Long noArticle, int propal);
    void mockEnchere();
    List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName);
    ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle);
}

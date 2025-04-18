package fr.eni.encheres.service.articleEnchere;

import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;

import java.util.List;

public interface ArticleEnchereInterface {

    List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres();
    void addEnchere(String userName, Long noArticle, int propal);
    void mockEnchere();
    List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName);
    ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle);
}

package fr.eni.encheres.service.articleEnchere;

import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;

import java.util.List;

public interface ArticleEnchereService {

    List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres();
    void addEnchere(String userName, Long noArticle, int propal);
    List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName);
    ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle);
    List<ArticleWithBestEnchereDTO> advancedSearch(String username, SearchCriteriaDTO criteria);

    void verifierEtFinaliserEncheres();
    ArticleWithBestEnchereDTO finaliserEnchere(Long noArticle);
}

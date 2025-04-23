package fr.eni.encheres.service.articleEnchere;


import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class ArticleEnchereServiceJPA implements ArticleEnchereService {

    private ArticleEnchereRepository articleEnchereRepository;

    @Override
    public List<ArticleWithBestEnchereDTO> getArticlesWithBestEncheres() {
        return List.of();
    }

    @Override
    public void addEnchere(String userName, Long noArticle, int propal) {

    }

    @Override
    public List<ArticleWithBestEnchereDTO> searchArticles(Long noCategorie, String searchName) {
        return List.of();
    }

    @Override
    public ArticleWithBestEnchereDTO getArticleWithBestEnchere(Long noArticle) {
        return null;
    }

    @Override
    public List<ArticleWithBestEnchereDTO> advancedSearch(String username, SearchCriteriaDTO criteria) {
        return List.of();
    }

    @Override
    public void verifierEtFinaliserEncheres() {

    }

    @Override
    public ArticleWithBestEnchereDTO finaliserEnchere(Long noArticle) {
        return null;
    }
}

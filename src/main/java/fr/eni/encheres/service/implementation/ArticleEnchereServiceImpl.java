package fr.eni.encheres.service.implementation;

import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.articleEnchere.ArticleEnchereService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleEnchereServiceImpl {

    @Autowired
    private ArticleEnchereService articleEnchereService;



    public ServiceResponse<List<ArticleWithBestEnchereDTO>> getAllArticlesWithBestEncheres() {
        List<ArticleWithBestEnchereDTO> allArticlesWithBestEncheres = articleEnchereService.getArticlesWithBestEncheres();

        if (allArticlesWithBestEncheres == null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Liste des articles avec leur meilleur enchère vide",null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Liste des articles avec leur meilleure enchere récupérées",allArticlesWithBestEncheres);
    }

    public ServiceResponse<List<ArticleWithBestEnchereDTO>> searchArticlesWithBestEncheres(Long noCategorie, String searchName){
        List<ArticleWithBestEnchereDTO> articlesWithBestEnchereResult = articleEnchereService.searchArticles(noCategorie, searchName);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Liste des articles avec leur meilleure enchere récupérées selon critères",articlesWithBestEnchereResult);
    }



    public ServiceResponse<ArticleWithBestEnchereDTO> articlesWithBestEnchere(Long noArticle){
        ArticleWithBestEnchereDTO articleWithBestEnchere = articleEnchereService.getArticleWithBestEnchere(noArticle);
        if (articleWithBestEnchere==null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Article avec meilleur enchère non existant",null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article n°"+noArticle+" avec meilleur enchere récupéré",articleWithBestEnchere);
    }

    public ServiceResponse<String> addEnchere(String userName, Long noArticle, int propal){
        // vérif de l'id

        articleEnchereService.addEnchere(userName, noArticle, propal);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"L'enchere a été créé","ok");
    }

    public ServiceResponse<List<ArticleWithBestEnchereDTO>> advancedSearch(String username, SearchCriteriaDTO criteria) {
        List<ArticleWithBestEnchereDTO> result = articleEnchereService.advancedSearch(username,criteria);

        return ServiceResponse.buildResponse(
                ServiceConstant.CD_SUCCESS,
                "Recherche avancée effectuée avec succès",
                result
        );
    }

}
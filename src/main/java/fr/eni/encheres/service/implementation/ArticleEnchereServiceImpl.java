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
        List<ArticleWithBestEnchereDTO> allBestEncheres = articleEnchereService.getArticlesWithBestEncheres();

        if (allBestEncheres == null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Liste des meilleures encheres vide",null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Liste des meilleures encheres récupérées",allBestEncheres);
    }

    public ServiceResponse<List<ArticleWithBestEnchereDTO>> searchArticlesWithBestEnchere(Long noCategorie, String searchName){
        List<ArticleWithBestEnchereDTO> articlesWithBestEnchereResult = articleEnchereService.searchArticles(noCategorie, searchName);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Liste des meilleures encheres récupérées selon critères",articlesWithBestEnchereResult);
    }



    public ServiceResponse<ArticleWithBestEnchereDTO> articlesWithBestEnchere(Long noArticle){
        ArticleWithBestEnchereDTO bestEnchereOnArticle = articleEnchereService.getArticleWithBestEnchere(noArticle);
        if (bestEnchereOnArticle==null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Meilleure enchere sur l'article non existante",null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Meilleure enchere sur l'article "+ noArticle +" récupérée",bestEnchereOnArticle);
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
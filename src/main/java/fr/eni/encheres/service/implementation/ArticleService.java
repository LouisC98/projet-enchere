package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.article.ArticleServiceInterface;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private ArticleServiceInterface articleService;

    public ArticleService(ArticleServiceInterface articleService) {
        this.articleService = articleService;
    }

    public ServiceResponse<List<ArticleVendu>> getAllArticles(){
        List<ArticleVendu> articleVenduList = articleService.getArticles();

        if(articleVenduList.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Liste vide",null);

        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Liste est bonne",articleVenduList);
    }

    public ServiceResponse<ArticleVendu> getArticleById(long noArticle){
        ArticleVendu articleVendu = articleService.getArticle(noArticle);
        if(articleVendu == null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucun article correspondant",null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article récupéré",articleVendu);
    }

    public ServiceResponse<String> addArticle(ArticleVendu article, String username){
        articleService.creerArticle(article, username);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article créé par : " + username, article.getNomArticle());
    }

//    public ServiceResponse<List<ArticleVendu>> SearchArticlesVendu(Long noCategorie, String searchName){
//        List<ArticleVendu> result = articleService.(noCategorie, searchName);
//        System.out.println( noCategorie+ searchName+ result);
//
//        if (result.isEmpty()) {
//            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucun article trouvé", null);
//        }
//
//        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "Liste d'articles trouvée", result);
//    }
}

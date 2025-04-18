package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.service.ArticleEnchereService;
import fr.eni.encheres.service.articleEnchere.ArticleEnchereInterface;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleEnchereServiceImpl {

    private ArticleEnchereInterface articleEnchereService;

    public ArticleEnchereServiceImpl(ArticleEnchereInterface articleEnchereService) {
        this.articleEnchereService = articleEnchereService;
    }

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
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Meilleurs enchere sur l'article non existantes",null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Meilleurs enchere sur l'article récupérée",bestEnchereOnArticle);
    }

    public ServiceResponse<String> addEnchere(String userName, Long noArticle, int propal){
        // vérif de l'id
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"L'enchere a été créé","Ciye");
    }

}
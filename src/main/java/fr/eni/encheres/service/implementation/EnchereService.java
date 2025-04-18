package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.enchere.EnchereServiceInterface;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnchereService {

    private EnchereServiceInterface enchereService;

    private ArticleService articleService;

    public EnchereService(EnchereServiceInterface enchereService, ArticleService articleService) {
        this.enchereService = enchereService;
        this.articleService = articleService;
    }

    public ServiceResponse<List<Enchere>> getAllEncheres(){
        List<Enchere> encheres = enchereService.getEncheres();

        if(encheres.isEmpty()){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Aucune enchère existante",encheres);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"La liste de toutes les enchères a bien été récupérée",encheres);
    }

    public ServiceResponse<List<Enchere>> getEnchereByArticle(Long noArticle){
        List<Enchere> encheres = enchereService.getEncheresByNoArticle(noArticle);

        if(encheres.isEmpty()){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Aucune enchère existante sur cette article",encheres);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"La liste des encheres sur l'article "+ noArticle +" a été récupérée",encheres);
    }


    //To change
    public ServiceResponse<String> addEnchere(Enchere enchere){
        ArticleVendu articleVendu = articleService.getArticleById(enchere.getArticleVendu().getNoArticle()).data;
        if (articleVendu != null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"L'article n'existe pas", null);
        }

        enchereService.addEnchere(enchere);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'enchere a bien été créé", "");
    }

    public ServiceResponse<Enchere> getMaxEnchere(Long noArticle){
        Enchere enchereMax = enchereService.getMaxEnchereByNoArticle(noArticle);

        //Rajouter une erreur si pas d'enchere max
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'enchere a bien été créé", enchereMax);
    }



}

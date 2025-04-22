package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.enchere.EnchereService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnchereServiceImpl {

    @Autowired
    private EnchereService enchereService;

    @Autowired
    private ArticleServiceImpl articleService;

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
        if (articleVendu == null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"L'article n'existe pas", null);
        }

        enchereService.addEnchere(enchere);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'enchere "+enchere.getArticleVendu().getNomArticle() + " a bien été créé", "");
    }

    public ServiceResponse<Enchere> getMaxEnchere(Long noArticle){
        Enchere enchereMax = enchereService.getMaxEnchereByNoArticle(noArticle);
        if(enchereMax==null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucune enchere sur " + noArticle + " a été trouvé",null);
        }
        //Rajouter une erreur si pas d'enchere max
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'enchere "+ enchereMax.getArticleVendu().getNomArticle() + " est existante", enchereMax);
    }

    public ServiceResponse<Enchere> updateEnchere(Enchere enchere){
        // Mettre à jour l'enchère via le service mock
        enchereService.updateEnchere(enchere);

        // Récupérer l'enchère mise à jour
        Enchere enchereUpdated = enchereService.getMaxEnchereByNoArticle(enchere.getArticleVendu().getNoArticle());

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,
                "L'enchère sur " + enchere.getArticleVendu().getNomArticle() + " a été mise à jour", enchereUpdated);
    }
}

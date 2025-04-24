package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.EtatVente;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.article.ArticleService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl {

    @Autowired
    private ArticleService articleService;

    public ServiceResponse<List<ArticleVendu>> getAllArticles(){
        List<ArticleVendu> articleVenduList = articleService.getArticles();

        if(articleVenduList.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Liste vide",null);

        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"La liste des articles a été récupéré",articleVenduList);
    }

    public ServiceResponse<ArticleVendu> getArticleById(long noArticle){
        ArticleVendu articleVendu = articleService.getArticle(noArticle);
        if(articleVendu == null) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucun article correspondant",null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article " + noArticle + " récupéré",articleVendu);
    }

    public ServiceResponse<String> addArticle(ArticleVendu article, String username){
        articleService.creerArticle(article, username);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article créé par : " + username, article.getNomArticle());
    }

    public ServiceResponse<ArticleVendu> updateEtatVente(Long noArticle) {
        ArticleVendu article = articleService.getArticle(noArticle);

        if (article == null) {
            return ServiceResponse.buildResponse(
                    ServiceConstant.CD_ERR_NOT_FOUND,
                    "Article non trouvé",
                    null
            );
        }

        article = articleService.updateEtatVente(article);

        return ServiceResponse.buildResponse(
                ServiceConstant.CD_SUCCESS,
                "État de l'article mis à jour avec succès",
                article
        );
    }

    public ServiceResponse<String> validerRetrait(Long noArticle, Utilisateur utilisateur) {
        // Récupérer l'article pour vérifier qu'il existe
        ArticleVendu article = articleService.getArticle(noArticle);

        if (article == null) {
            return ServiceResponse.buildResponse(
                    ServiceConstant.CD_ERR_NOT_FOUND,
                    "Article non trouvé",
                    null
            );
        }

        // Vérifier l'état de l'article
        if (!EtatVente.VENDU.equals(article.getEtatVente())) {
            return ServiceResponse.buildResponse(
                    ServiceConstant.CD_ERR_UNKNOWN,
                    "L'article n'est pas en état 'VENDU'",
                    null
            );
        }

        // Vérifier que l'utilisateur est bien l'acheteur
        if (!utilisateur.equals(article.getAcheteur())) {
            return ServiceResponse.buildResponse(
                    ServiceConstant.CD_ERR_UNKNOWN,
                    "Vous n'êtes pas l'acheteur de cet article",
                    null
            );
        }

        // Appeler la méthode du service pour valider le retrait
        articleService.validerRetrait(noArticle, utilisateur);

        return ServiceResponse.buildResponse(
                ServiceConstant.CD_SUCCESS,
                "Retrait validé avec succès",
                article.getNomArticle()
        );
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

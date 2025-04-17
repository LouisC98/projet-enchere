package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.ArticleServiceInterface;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.stereotype.Service;

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

    public ServiceResponse<String> addArticle(ArticleVendu article){
        articleService.creerArticle(article);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Article créé", article.getNomArticle());
    }

    public ServiceResponse<List<ArticleVendu>> SearchArticlesVendu(Long noCategorie, String searchName){
        List<ArticleVendu> result = new ArrayList<>(articleService.getArticles());

        if (noCategorie != null) {
            result = result.stream()
                    .filter(article -> article.getCategorie().getNoCategorie().equals(noCategorie))
                    .collect(Collectors.toList());
        }

        if (searchName != null && !searchName.trim().isEmpty()) {
            String searchNameLower = searchName.toLowerCase();
            result = result.stream()
                    .filter(article -> article.getNomArticle().toLowerCase().contains(searchNameLower))
                    .collect(Collectors.toList());
        }

        if (result.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucun article trouvé", null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "Liste d'articles trouvée", result);
    }
}

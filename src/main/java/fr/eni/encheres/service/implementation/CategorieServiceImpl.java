package fr.eni.encheres.service.implementation;


import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.service.categorie.CategorieService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieServiceImpl {

    @Autowired
    CategorieService categorieService;

    public ServiceResponse<Categorie> getCategorie(Long noCategorie){
        Categorie categorieRecherche = categorieService.getCategorie(noCategorie);

        if(categorieRecherche == null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucune catégorie correspondante à l'id " + noCategorie, null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "La catégorie correspondant à l'id " + noCategorie + "(" + categorieRecherche.getLibelle()+ ") a été trouvé", categorieRecherche);
    }

    public ServiceResponse<List<Categorie>> getAllCategorie(){
        List<Categorie> categorieList = categorieService.getCategories();

        if(categorieList == null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucune catégorie n'a été créé",null);
        }

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "La liste des catégories a été récupéré",categorieList);
    }

    public ServiceResponse<Categorie> addCategorie(Categorie categorie){
        List<Categorie> categorieList = categorieService.getCategories();
        if(categorieList.contains(categorie)){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "La catégorie existe déjà",null);
        }
        categorieService.addCategorie(categorie);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "La catégorie a été créé",categorie);
    }
}

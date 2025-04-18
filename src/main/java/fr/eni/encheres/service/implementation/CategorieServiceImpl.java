package fr.eni.encheres.service.implementation;


import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.service.CategorieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private List<Categorie> categories = new ArrayList<>();

    public CategorieServiceImpl() {
        mockCategories();
    }

    @Override
    public List<Categorie> getCategories() {
        return categories;
    }


    //A changer
    @Override
    public Categorie getCategorie(Long noCategorie) {
        return categories.stream().filter(
                categorie -> categorie.getNoCategorie().equals(noCategorie)
        ).findFirst().orElse(null);
    }

    public void mockCategories(){
        categories.add(new Categorie(Long.valueOf(123),"Maison"));
        categories.add(new Categorie(Long.valueOf(456),"Informatique"));
    }
}

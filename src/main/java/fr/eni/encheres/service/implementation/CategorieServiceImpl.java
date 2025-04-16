package fr.eni.encheres.service.implementation;


import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.service.CategorieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private List<Categorie> categories = new ArrayList<Categorie>();

    public CategorieServiceImpl() {
        categories.add(new Categorie("123","Maison"));
        categories.add(new Categorie("456","Informatique"));
    }

    @Override
    public List<Categorie> getCategories() {
        return categories;
    }


    //A changer
    @Override
    public Categorie getCategorie(int id) {
        return categories.get(id);
    }

}

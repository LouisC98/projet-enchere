package fr.eni.encheres.service.categorie;


import fr.eni.encheres.bo.Categorie;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("Dev")
public class CategorieServiceMock implements CategorieService {

    private List<Categorie> categories = new ArrayList<>();

    public CategorieServiceMock() {
        mockCategories();
    }
    private static long INDEX = 2;
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


    @Override
    public void addCategorie(Categorie categorie) {

        if (!categories.stream().anyMatch(c -> c.getLibelle().equalsIgnoreCase(categorie.getLibelle()))) {
            categories.add(new Categorie(INDEX++,categorie.getLibelle()));
        }
    }



}

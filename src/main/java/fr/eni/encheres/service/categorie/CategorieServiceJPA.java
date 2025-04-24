package fr.eni.encheres.service.categorie;

import fr.eni.encheres.bo.Categorie;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class CategorieServiceJPA implements CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @PostConstruct
    private void mockBdd() {
        if (categorieRepository.count() == 0) {
            Categorie c1 = new Categorie();
            c1.setLibelle("Maison");

            Categorie c2 = new Categorie();
            c2.setLibelle("Informatique");

            categorieRepository.save(c1);
            categorieRepository.save(c2);
        }
    }

    @Override
    public List<Categorie> getCategories() {
        List<Categorie> liste = categorieRepository.findAll();

        return categorieRepository.findAll();
    }

    @Override
    public Categorie getCategorie(Long noCategorie) {
        return categorieRepository.findByNoCategorie(noCategorie);
    }
}

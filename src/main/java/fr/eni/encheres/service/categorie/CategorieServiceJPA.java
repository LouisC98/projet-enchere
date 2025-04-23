package fr.eni.encheres.service.categorie;

import fr.eni.encheres.bo.Categorie;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class CategorieServiceJPA implements CategorieService {

    private CategorieRepository categorieRepository;


    public CategorieServiceJPA(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
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

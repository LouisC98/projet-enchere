package fr.eni.encheres.service.categorie;


import fr.eni.encheres.bo.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CategorieRepository extends JpaRepository<Categorie,Long> {

    Categorie findByNoCategorie(Long noCategorie);
}

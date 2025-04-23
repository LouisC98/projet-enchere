package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.EtatVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ArticleRepository extends JpaRepository<ArticleVendu,Long> {
    List<ArticleVendu> findByEtatVente(EtatVente etatVente);
    List<ArticleVendu> findByVendeurId(Integer vendeurId);
}
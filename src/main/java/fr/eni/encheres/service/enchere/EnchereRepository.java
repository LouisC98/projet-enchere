package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface EnchereRepository extends JpaRepository<Enchere, Long> {
    List<Enchere> findByArticleVenduNoArticle(Long noArticle);

    @Query("SELECT e FROM Enchere e WHERE e.articleVendu.noArticle = :noArticle ORDER BY e.montantEnchere DESC LIMIT 1")
    Enchere findMaxEnchereByNoArticle(@Param("noArticle") Long noArticle);
}
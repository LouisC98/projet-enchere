package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.EtatVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ArticleRepository extends JpaRepository<ArticleVendu,Long> {
    List<ArticleVendu> findByEtatVente(EtatVente etatVente);
    List<ArticleVendu> findByVendeurId(Integer vendeurId);
    List<ArticleVendu> findByEtatVenteAndCategorieNoCategorie(EtatVente etatVente, Long noCategorie);

    @Query("SELECT a FROM ArticleVendu a WHERE a.etatVente = :etatVente AND LOWER(a.nomArticle) LIKE LOWER(CONCAT('%', :nomArticle, '%'))")
    List<ArticleVendu> findByEtatVenteAndNomArticleContaining(@Param("etatVente") EtatVente etatVente, @Param("nomArticle") String nomArticle);

    @Query("SELECT a FROM ArticleVendu a WHERE a.etatVente = :etatVente AND a.categorie.noCategorie = :categorieId AND LOWER(a.nomArticle) LIKE LOWER(CONCAT('%', :nomArticle, '%'))")
    List<ArticleVendu> findByEtatVenteAndCategorieAndNomArticleContaining(
            @Param("etatVente") EtatVente etatVente,
            @Param("categorieId") Long categorieId,
            @Param("nomArticle") String nomArticle);

    @Query("SELECT a FROM ArticleVendu a WHERE a.dateFinEnchere < :now AND a.etatVente = fr.eni.encheres.bo.EtatVente.EN_COURS")
    List<ArticleVendu> findArticlesToFinalize(@Param("now") LocalDateTime now);

    List<ArticleVendu> findByVendeurIdAndEtatVente(Integer vendeurId, EtatVente etatVente);

    @Query("SELECT a FROM ArticleVendu a WHERE a.acheteur.id = :acheteurId AND a.etatVente IN :etats")
    List<ArticleVendu> findByAcheteurIdAndEtatVenteIn(@Param("acheteurId") Integer acheteurId, @Param("etats") List<EtatVente> etats);

    @Query("SELECT a FROM ArticleVendu a WHERE a.vendeur.id != :vendeurId AND a.etatVente = :etatVente")
    List<ArticleVendu> findByVendeurIdNotAndEtatVente(@Param("vendeurId") Integer vendeurId, @Param("etatVente") EtatVente etatVente);

    @Query("SELECT a FROM ArticleVendu a WHERE a.vendeur.id = :vendeurId AND a.etatVente IN :etats")
    List<ArticleVendu> findByVendeurIdAndEtatVenteIn(@Param("vendeurId") Integer vendeurId, @Param("etats") List<EtatVente> etats);

    List<ArticleVendu> findByEtatVenteAndDateDebutEncheresLessThan(EtatVente etatVente, LocalDateTime dateTime);
}
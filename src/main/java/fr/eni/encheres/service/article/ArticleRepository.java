package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ArticleRepository extends JpaRepository<ArticleVendu,Long> {
}

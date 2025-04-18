package fr.eni.encheres.dto;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleWithBestEnchereDTO {
    private ArticleVendu article;
    private Enchere bestEnchere;
}
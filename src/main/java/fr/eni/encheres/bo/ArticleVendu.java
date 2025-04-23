package fr.eni.encheres.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ArticleVendu {

    private Long noArticle;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEnchere;
    private int misAPrix;
    private int prixVente;

    private EtatVente etatVente = EtatVente.CREEE;

    private Categorie categorie;
    private Enchere enchere;
    private Retrait retrait;
    private Utilisateur acheteur;
    private Utilisateur vendeur;
    private String imageName;
}

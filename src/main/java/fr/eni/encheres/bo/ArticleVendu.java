package fr.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class ArticleVendu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noArticle;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEnchere;
    private int misAPrix;
    private int prixVente;

    @Enumerated(EnumType.ORDINAL)
    private EtatVente etatVente;

    @OneToOne
    private Categorie categorie;

    @OneToOne
    private Enchere enchere;

    @OneToOne
    private Retrait retrait;

    @OneToOne
    private Utilisateur acheteur;
    @OneToOne
    private Utilisateur vendeur;
    private String imageName;
}

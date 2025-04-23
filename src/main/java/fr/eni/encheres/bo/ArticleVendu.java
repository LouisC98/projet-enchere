package fr.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String imageName;

    @Enumerated(EnumType.ORDINAL)
    private EtatVente etatVente;

    @ManyToOne
    @JoinColumn(name = "noCategorie")
    private Categorie categorie;

    @OneToMany(mappedBy = "articleVendu")
    private List<Enchere> encheres = new ArrayList<>();

    @OneToOne(mappedBy = "articleVendu", cascade = CascadeType.ALL)
    private Retrait retrait;

    @ManyToOne
    @JoinColumn(name = "acheteur_id")
    private Utilisateur acheteur;

    @ManyToOne
    @JoinColumn(name = "vendeur_id")
    private Utilisateur vendeur;
}

package fr.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noCategorie;
    private String libelle;

    @OneToMany(mappedBy = "categorie")
    private List<ArticleVendu> articlesVendusListe = new ArrayList<>();


    //Constructeur sans les autres classes
    public Categorie(Long noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    public Categorie(Long noCategorie, String libelle, List<ArticleVendu> articlesVendusListe) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
        this.articlesVendusListe = articlesVendusListe;
    }
}

package fr.eni.encheres.bo;

import lombok.Data;

import java.util.List;

@Data
public class Categorie {

    private Long noCategorie;
    private String libelle;

    private List<ArticleVendu> articlesVendusListe;


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

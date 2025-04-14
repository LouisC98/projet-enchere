package fr.eni.encheres.bo;

import lombok.Data;

import java.util.List;

@Data
public class Categorie {

    private String noCategorie;
    private String libelle;

    private List<ArticleVendu> articlesVendusListe;


    //Constructeur sans les autres classes
    public Categorie(String noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }

    public Categorie(String noCategorie, String libelle, List<ArticleVendu> articlesVendusListe) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
        this.articlesVendusListe = articlesVendusListe;
    }
}

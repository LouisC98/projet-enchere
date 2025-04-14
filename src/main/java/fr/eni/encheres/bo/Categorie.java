package fr.eni.encheres.bo;

import lombok.Data;

@Data
public class Categorie {

    private String noCategorie;
    private String libelle;

    public Categorie(String noCategorie, String libelle) {
        this.noCategorie = noCategorie;
        this.libelle = libelle;
    }
}

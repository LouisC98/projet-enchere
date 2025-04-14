package fr.eni.encheres.bo;

import lombok.Data;

import java.util.Optional;

@Data
public class Retrait {

    private String rue;
    private int codePostal;
    private String ville;

    private Optional<ArticleVendu> articleVendu;


    //Constructeur sans les autres classes
    public Retrait(String rue, int codePostal, String ville) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }

    public Retrait(String rue, int codePostal, String ville, Optional<ArticleVendu> articleVendu) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.articleVendu = articleVendu;
    }
}

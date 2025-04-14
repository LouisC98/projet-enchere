package fr.eni.encheres.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Enchere {

    private Date dateEnchere;
    private int montantEnchere;
    private ArticleVendu articleVendu;
    //private Utilisateur encherisseur;


    //Constructeur sans les autres classes
    public Enchere(Date dateEnchere, int montantEnchere) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }


    //Add utilisateur dans le futur
    public Enchere(Date dateEnchere, int montantEnchere, ArticleVendu articleVendu) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.articleVendu = articleVendu;
    }
}

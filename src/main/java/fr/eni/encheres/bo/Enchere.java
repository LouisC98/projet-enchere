package fr.eni.encheres.bo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Data
public class Enchere {

    private LocalDateTime dateEnchere;
    private int montantEnchere;
    private ArticleVendu articleVendu;
    //private Utilisateur encherisseur;


    public Enchere(LocalDateTime dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    //Constructeur sans les autres classes
    public Enchere(LocalDateTime dateEnchere, int montantEnchere) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }


    //Add utilisateur dans le futur
    public Enchere(LocalDateTime dateEnchere, int montantEnchere, ArticleVendu articleVendu) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
        this.articleVendu = articleVendu;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    public void setArticleVendu(ArticleVendu articleVendu) {
        this.articleVendu = articleVendu;
    }

    public ArticleVendu getArticleVendu() {
        return articleVendu;
    }
}

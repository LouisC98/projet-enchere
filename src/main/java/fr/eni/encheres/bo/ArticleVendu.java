package fr.eni.encheres.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVendu {

    private String noArticle;
    private String nomArticle;
    private String description;
    private Date dateDebutEncheres;
    private Date DateFinEnchere;
    private int misAPrix;
    private int prixVente;
    //Voir si on fait pas une enum ici car toujours pareil
    private String etatVente;

    public ArticleVendu(String noArticle, String nomArticle, String description, Date dateDebutEncheres, Date dateFinEnchere, int misAPrix, int prixVente, String etatVente) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        DateFinEnchere = dateFinEnchere;
        this.misAPrix = misAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
    }
}

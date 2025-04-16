package fr.eni.encheres.bo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ArticleVendu {

    private String noArticle;
    private String nomArticle;
    private String description;
    private LocalDateTime dateDebutEncheres;
    private LocalDateTime dateFinEnchere;
    private int misAPrix;
    private int prixVente;
    //Voir si on fait pas une enum ici car toujours pareil
    private String etatVente;


    private Categorie categorie;
    private Enchere enchere;
    private Retrait retrait;
    //private Utilisateur acheteur;
    //private Utilisateur vendeur;

    public ArticleVendu(String noArticle, String nomArticle, String description, Categorie categorie, int misAPrix, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEnchere, Retrait retrait) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.categorie = categorie;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEnchere = dateFinEnchere;
        this.misAPrix = misAPrix;
        this.retrait = retrait;
    }

    //Constructeur sans les autres classes
    public ArticleVendu(String noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEnchere, int misAPrix, int prixVente, String etatVente) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEnchere = dateFinEnchere;
        this.misAPrix = misAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
    }


    //Add utilisateur dans le futur
    public ArticleVendu(String noArticle, String nomArticle, String description, LocalDateTime dateDebutEncheres, LocalDateTime dateFinEnchere, int misAPrix, int prixVente, String etatVente, Categorie categorie, Enchere enchere, Retrait retrait) {
        this.noArticle = noArticle;
        this.nomArticle = nomArticle;
        this.description = description;
        this.dateDebutEncheres = dateDebutEncheres;
        this.dateFinEnchere = dateFinEnchere;
        this.misAPrix = misAPrix;
        this.prixVente = prixVente;
        this.etatVente = etatVente;
        this.categorie = categorie;
        this.enchere = enchere;
        this.retrait = retrait;
    }

}

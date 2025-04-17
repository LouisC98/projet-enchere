package fr.eni.encheres.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Utilisateur {
    private Integer id;
    private String pseudo;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String rue;
    private String codePostal;
    private String ville;
    private String motDePasse;
    private Integer credit;
    private Boolean administrateur;

    private List<ArticleVendu> articleVenduList = new ArrayList<>();
//    private List<ArticleVendu> articleAchatList;

    public Utilisateur() {
        this.credit = 0;
        this.administrateur = false;
    }

    public Utilisateur(String pseudo, String nom, String prenom, String email, 
                      String telephone, String rue, String codePostal, 
                      String ville, String motDePasse, Integer credit, Boolean administrateur) {
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
        this.motDePasse = motDePasse;
        this.credit = credit != null ? credit : 0;
        this.administrateur = administrateur != null ? administrateur : false;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", credit=" + credit +
                ", administrateur=" + administrateur +
                '}';
    }
}

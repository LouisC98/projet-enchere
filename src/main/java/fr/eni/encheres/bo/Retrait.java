package fr.eni.encheres.bo;

import lombok.Data;

@Data
public class Retrait {

    private String rue;
    private int codePostal;
    private String ville;

    public Retrait(String rue, int codePostal, String ville) {
        this.rue = rue;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}

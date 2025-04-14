package fr.eni.encheres.bo;

import lombok.Data;

import java.util.Date;


@Data
public class Enchere {

    private Date dateEnchere;
    private int montantEnchere;

    public Enchere(Date dateEnchere, int montantEnchere) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }
}

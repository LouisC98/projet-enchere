package fr.eni.encheres.bo;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class Enchere {

    private LocalDateTime dateEnchere;
    private int montantEnchere;
    private ArticleVendu articleVendu;
    private Utilisateur encherisseur;

}

package fr.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Entity
public class Enchere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateEnchere;
    private int montantEnchere;

    @ManyToOne
    @JoinColumn(name = "noArticle")
    private ArticleVendu articleVendu;

    @ManyToOne
    @JoinColumn(name = "encherisseur_id")
    private Utilisateur encherisseur;
}

package fr.eni.encheres.bo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@Entity
public class Retrait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rue;
    private String codePostal;
    private String ville;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "noArticle")
    private ArticleVendu articleVendu;
}

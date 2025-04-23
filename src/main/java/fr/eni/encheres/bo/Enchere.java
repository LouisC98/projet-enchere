package fr.eni.encheres.bo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@Entity
public class Enchere {

    private LocalDateTime dateEnchere;
    private int montantEnchere;

    @OneToOne
    private ArticleVendu articleVendu;

    @OneToOne
    private Utilisateur encherisseur;

    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

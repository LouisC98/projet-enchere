package fr.eni.encheres.service.user;

import fr.eni.encheres.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByPseudo(String pseudo);

    Optional<Utilisateur> findByEmail(String email);
}

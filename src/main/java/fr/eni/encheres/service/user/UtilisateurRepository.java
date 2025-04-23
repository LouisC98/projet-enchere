package fr.eni.encheres.service.user;

import fr.eni.encheres.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
}

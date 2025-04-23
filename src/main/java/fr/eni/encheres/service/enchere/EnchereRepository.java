package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface EnchereRepository extends JpaRepository<Enchere, Long> {
}

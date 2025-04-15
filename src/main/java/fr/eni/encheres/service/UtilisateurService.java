package fr.eni.encheres.service;

import java.util.List;
import java.util.Optional;

import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {
    Optional<Utilisateur> seConnecter(String pseudo, String motDePasse);

    Utilisateur sInscrire(Utilisateur utilisateur) throws Exception;

    Utilisateur modifierProfil(Utilisateur utilisateur) throws Exception;

    boolean supprimerCompte(Integer id);

    Optional<Utilisateur> getUtilisateurById(Integer id);

    Optional<Utilisateur> getUtilisateurByPseudo(String pseudo);

    List<Utilisateur> getAllUtilisateurs();

    Utilisateur ajouterCredit(Integer utilisateurId, Integer montant) throws Exception;

    boolean isPseudoExistant(String pseudo);

    boolean isEmailExistant(String email);

    Optional<Utilisateur> getUtilisateurByEmail(String email);
}

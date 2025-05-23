package fr.eni.encheres.service.user;

import java.util.List;
import java.util.Optional;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;

public interface UtilisateurService {


    Optional<Utilisateur> getUtilisateurById(Integer id);

    Optional<Utilisateur> getUtilisateurByPseudo(String pseudo);

    List<Utilisateur> getAllUtilisateurs();

    boolean isPseudoExistant(String pseudo);

    boolean isEmailExistant(String email);

    Optional<Utilisateur> getUtilisateurByEmail(String email);

    void addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre);

    void removeCredits(Utilisateur utilisateur, int montant);

    void addCredits(Utilisateur utilisateur, int montant);

    boolean deleteUtilisateur(Integer utilisateurId);

    Utilisateur ajouterUtilisateur(Utilisateur utilisateur);

    boolean updateUtilisateur(Utilisateur utilisateur);

    Optional<Utilisateur> authentifier(String login, String motDePasse);
}

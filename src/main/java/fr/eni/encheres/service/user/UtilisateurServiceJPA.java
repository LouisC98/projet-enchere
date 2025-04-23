package fr.eni.encheres.service.user;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("Prod")
@Repository
public class UtilisateurServiceJPA implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    @Override
    public Optional<Utilisateur> seConnecter(String pseudo, String motDePasse) {
        return Optional.empty();
    }

    @Override
    public Utilisateur sInscrire(Utilisateur utilisateur) throws Exception {
        return null;
    }

    @Override
    public Utilisateur modifierProfil(Utilisateur utilisateur) throws Exception {
        return null;
    }

    @Override
    public boolean supprimerCompte(Integer id) {
        return false;
    }

    @Override
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return Optional.empty();
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return List.of();
    }

    @Override
    public Utilisateur ajouterCredit(Integer utilisateurId, Integer montant) throws Exception {
        return null;
    }

    @Override
    public boolean isPseudoExistant(String pseudo) {
        return false;
    }

    @Override
    public boolean isEmailExistant(String email) {
        return false;
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre) {

    }

    @Override
    public void removeCredits(Utilisateur utilisateur, int montant) {

    }

    @Override
    public void addCredits(Utilisateur utilisateur, int montant) {

    }

    @Override
    public boolean deleteUtilisateur(Integer utilisateurId) {
        return false;
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        return null;
    }

    @Override
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        return false;
    }

    @Override
    public Optional<Utilisateur> authentifier(String login, String motDePasse) {
        return Optional.empty();
    }
}

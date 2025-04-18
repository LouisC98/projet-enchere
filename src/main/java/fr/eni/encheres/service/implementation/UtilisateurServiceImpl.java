package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bll.UtilisateurMock;
import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.UtilisateurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Override
    public Optional<Utilisateur> seConnecter(String pseudo, String motDePasse) {
        return UtilisateurMock.authentifier(pseudo, motDePasse);
    }

    @Override
    public Utilisateur sInscrire(Utilisateur utilisateur) throws Exception {
        if (isPseudoExistant(utilisateur.getPseudo())) {
            throw new Exception("Ce pseudo est déjà utilisé.");
        }
        if (isEmailExistant(utilisateur.getEmail())) {
            throw new Exception("Cet e-mail est déjà utilisé.");
        }
        Utilisateur nouvelUtilisateur = UtilisateurMock.ajouterUtilisateur(utilisateur);
        return nouvelUtilisateur;
    }

    @Override
    public Utilisateur modifierProfil(Utilisateur utilisateur) throws Exception {
        boolean updated = UtilisateurMock.updateUtilisateur(utilisateur);
        if (!updated) {
            throw new Exception("Impossible de mettre à jour l'utilisateur (ID inexistant ?).");
        }
        return utilisateur;
    }

    @Override
    public boolean supprimerCompte(Integer id) {
        return UtilisateurMock.deleteUtilisateur(id);
    }

    @Override
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return UtilisateurMock.getUtilisateurById(id);
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return UtilisateurMock.getUtilisateurByPseudo(pseudo);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return UtilisateurMock.getAllUtilisateurs();
    }

    @Override
    public Utilisateur ajouterCredit(Integer utilisateurId, Integer montant) throws Exception {
        Optional<Utilisateur> optUser = UtilisateurMock.getUtilisateurById(utilisateurId);
        if (optUser.isEmpty()) {
            throw new Exception("Utilisateur inexistant (ID: " + utilisateurId + ").");
        }
        Utilisateur user = optUser.get();
        user.setCredit(user.getCredit() + montant);
        UtilisateurMock.updateUtilisateur(user);
        return user;
    }

    @Override
    public boolean isPseudoExistant(String pseudo) {
        return UtilisateurMock.isPseudoExistant(pseudo);
    }

    @Override
    public boolean isEmailExistant(String email) {
        return UtilisateurMock.isEmailExistant(email);
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return UtilisateurMock.getUtilisateurByEmail(email);
    }

    public void addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre) {
        utilisateur.getArticleVenduList().add(articleAVendre);
    }

    @Override
    public void removeCredits(Utilisateur utilisateur, int montant) {
        if (montant <= utilisateur.getCredit()) {
            utilisateur.setCredit(utilisateur.getCredit() - montant);
        }
    }
}

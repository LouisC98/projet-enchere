package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.user.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl {

    
    @Autowired
    UtilisateurService utilisateurService;
    
    public Optional<Utilisateur> seConnecter(String pseudo, String motDePasse) {
        return utilisateurService.authentifier(pseudo, motDePasse);
    }

    
    public Utilisateur sInscrire(Utilisateur utilisateur) throws Exception {
        if (isPseudoExistant(utilisateur.getPseudo())) {
            throw new Exception("Ce pseudo est déjà utilisé.");
        }
        if (isEmailExistant(utilisateur.getEmail())) {
            throw new Exception("Cet e-mail est déjà utilisé.");
        }
        Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);
        return nouvelUtilisateur;
    }

    
    public Utilisateur modifierProfil(Utilisateur utilisateur) throws Exception {
        boolean updated = utilisateurService.updateUtilisateur(utilisateur);
        if (!updated) {
            throw new Exception("Impossible de mettre à jour l'utilisateur (ID inexistant ?).");
        }
        return utilisateur;
    }

    
    public boolean supprimerCompte(Integer id) {
        return utilisateurService.deleteUtilisateur(id);
    }

    
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurService.getUtilisateurById(id);
    }

    
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return utilisateurService.getUtilisateurByPseudo(pseudo);
    }

    
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    
    public Utilisateur ajouterCredit(Integer utilisateurId, Integer montant) throws Exception {
        Optional<Utilisateur> optUser = utilisateurService.getUtilisateurById(utilisateurId);
        if (optUser.isEmpty()) {
            throw new Exception("Utilisateur inexistant (ID: " + utilisateurId + ").");
        }
        Utilisateur user = optUser.get();
        user.setCredit(user.getCredit() + montant);
        utilisateurService.updateUtilisateur(user);
        return user;
    }

    
    public boolean isPseudoExistant(String pseudo) {
        return utilisateurService.isPseudoExistant(pseudo);
    }

    
    public boolean isEmailExistant(String email) {
        return utilisateurService.isEmailExistant(email);
    }

    
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurService.getUtilisateurByEmail(email);
    }

    public void addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre) {
        utilisateur.getArticleVenduList().add(articleAVendre);
    }

    
    public void removeCredits(Utilisateur utilisateur, int montant) {
        if (montant <= utilisateur.getCredit()) {
            utilisateur.setCredit(utilisateur.getCredit() - montant);
        }
    }

    
    public void addCredits(Utilisateur utilisateur, int montant) {
        utilisateur.setCredit(utilisateur.getCredit() + montant);
    }
}

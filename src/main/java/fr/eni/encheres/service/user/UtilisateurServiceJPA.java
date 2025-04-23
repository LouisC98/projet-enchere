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

    public UtilisateurServiceJPA(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    @Override
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return utilisateurRepository.findByPseudo(pseudo);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }


    @Override
    public boolean isPseudoExistant(String pseudo) {
        return utilisateurRepository.findByPseudo(pseudo).isPresent();
    }

    @Override
    public boolean isEmailExistant(String email) {
        return utilisateurRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public void addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre) {
        utilisateur.getArticleVenduList().add(articleAVendre);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public void removeCredits(Utilisateur utilisateur, int montant) {
        if(utilisateur.getCredit() >= montant) {
            utilisateur.setCredit(utilisateur.getCredit() - montant);
            utilisateurRepository.save(utilisateur);
        }
    }

    @Override
    public void addCredits(Utilisateur utilisateur, int montant) {
        utilisateur.setCredit(utilisateur.getCredit() + montant);
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public boolean deleteUtilisateur(Integer utilisateurId) {
        return false;
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findById(utilisateur.getId()).isPresent();
    }

    @Override
    public Optional<Utilisateur> authentifier(String login, String motDePasse) {
        return Optional.empty();
    }
}

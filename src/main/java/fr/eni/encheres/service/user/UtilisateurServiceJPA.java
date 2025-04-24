package fr.eni.encheres.service.user;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Profile("Prod")
@Repository
public class UtilisateurServiceJPA implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostConstruct
    private void mockBdd() {
        if (utilisateurRepository.count() == 0) {
            // Création de 2 utilisateurs normaux
            Utilisateur user1 = new Utilisateur();
            user1.setPseudo("johndoe");
            user1.setNom("Doe");
            user1.setPrenom("John");
            user1.setEmail("john.doe@example.com");
            user1.setTelephone("0123456789");
            user1.setRue("123 Rue de Paris");
            user1.setCodePostal("75001");
            user1.setVille("Paris");
            user1.setMotDePasse(passwordEncoder.encode("azer"));
            user1.setCredit(100);
            user1.setAdministrateur(false);
            user1.setSuppr(false);

            Utilisateur user2 = new Utilisateur();
            user2.setPseudo("janedoe");
            user2.setNom("Doe");
            user2.setPrenom("Jane");
            user2.setEmail("jane.doe@example.com");
            user2.setTelephone("0987654321");
            user2.setRue("456 Avenue de Lyon");
            user2.setCodePostal("69001");
            user2.setVille("Lyon");
            user2.setMotDePasse(passwordEncoder.encode("azer"));
            user2.setCredit(150);
            user2.setAdministrateur(false);
            user2.setSuppr(false);

            // Création d'un administrateur
            Utilisateur admin = new Utilisateur();
            admin.setPseudo("admin");
            admin.setNom("Admin");
            admin.setPrenom("Super");
            admin.setEmail("admin@example.com");
            admin.setTelephone("0654321098");
            admin.setRue("789 Boulevard Administrateur");
            admin.setCodePostal("44000");
            admin.setVille("Nantes");
            admin.setMotDePasse(passwordEncoder.encode("azer"));
            admin.setCredit(500);
            admin.setAdministrateur(true);
            admin.setSuppr(false);

            // Sauvegarde des utilisateurs dans la base de données
            utilisateurRepository.save(user1);
            utilisateurRepository.save(user2);
            utilisateurRepository.save(admin);
        }
    }


    @Override
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return utilisateurRepository.findUtilisateurByPseudo(pseudo);
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }


    @Override
    public boolean isPseudoExistant(String pseudo) {
        return utilisateurRepository.findUtilisateurByPseudo(pseudo).isPresent();
    }

    @Override
    public boolean isEmailExistant(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email).isPresent();
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email);
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
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);
        utilisateur.setSuppr(false);
        utilisateurRepository.save(utilisateur);
        return true;
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        if(utilisateurRepository.findById(utilisateur.getId()).isPresent()){
            utilisateurRepository.save(utilisateur);
        }else{
            return false;
        }
        return true ;
    }

    @Override
    public Optional<Utilisateur> authentifier(String login, String motDePasse) {
        return Optional.empty();
    }
}

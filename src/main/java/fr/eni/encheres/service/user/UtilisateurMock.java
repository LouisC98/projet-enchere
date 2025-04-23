package fr.eni.encheres.service.user;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("Dev")
public class UtilisateurMock implements UtilisateurService {


    private static final Map<Integer, Utilisateur> utilisateurs = new HashMap<>();
    private static int nextId = 1;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UtilisateurMock() {
        mockUser();
    }

    public void mockUser() {
        ajouterUtilisateur(new Utilisateur("jdupont", "Dupont", "Jean", "jean.dupont@example.com",
                "0123456789", "123 Rue de Paris", "75001", "Paris", passwordEncoder.encode("motdepasse1"), 100, false,
                false));

        ajouterUtilisateur(new Utilisateur("mmartin", "Martin", "Marie", "marie.martin@example.com",
                "0987654321", "456 Avenue des Champs", "69002", "Lyon", passwordEncoder.encode("motdepasse2"), 200,
                false, false));

        ajouterUtilisateur(new Utilisateur("admin", "admin", "admin", "admin@example.com",
                "0123123123", "789 Boulevard Admin", "35000", "Rennes", passwordEncoder.encode("admin"), 4000, true,
                false));
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateur.setId(nextId++);
        utilisateurs.put(utilisateur.getId(), utilisateur);
        return utilisateur;
    }

    @Override
    public List<Utilisateur> getAllUtilisateurs() {
        return new ArrayList<>(utilisateurs.values());
    }

    @Override
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return Optional.ofNullable(utilisateurs.get(id));
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return utilisateurs.values().stream()
                .filter(u -> u.getPseudo().equals(pseudo))
                .findFirst();
    }

    @Override
    public Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurs.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getId() == null || !utilisateurs.containsKey(utilisateur.getId())) {
            return false;
        }
        utilisateurs.put(utilisateur.getId(), utilisateur);
        return true;
    }

    @Override
    public boolean deleteUtilisateur(Integer id) {
        return utilisateurs.remove(id) != null;
    }

    @Override
    public Optional<Utilisateur> authentifier(String login, String motDePasse) {
        return utilisateurs.values().stream()
                .filter(u -> (u.getPseudo().equals(login) || u.getEmail().equals(login)))
                .findFirst();
    }

    @Override
    public boolean isPseudoExistant(String pseudo) {
        return utilisateurs.values().stream()
                .anyMatch(u -> u.getPseudo().equals(pseudo));
    }

    @Override
    public boolean isEmailExistant(String email) {
        return utilisateurs.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }



    public void reset() {
        utilisateurs.clear();
        nextId = 1;
    }

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
    public Utilisateur ajouterCredit(Integer utilisateurId, Integer montant) throws Exception {
        return null;
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

}
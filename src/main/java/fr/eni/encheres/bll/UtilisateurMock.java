package fr.eni.encheres.bll;

import fr.eni.encheres.bo.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UtilisateurMock {

    private static final Map<Integer, Utilisateur> utilisateurs = new HashMap<>();
    private static int nextId = 1;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    static {
        ajouterUtilisateur(new Utilisateur("jdupont", "Dupont", "Jean", "jean.dupont@example.com",
                "0123456789", "123 Rue de Paris", "75001", "Paris", passwordEncoder.encode("motdepasse1"), 100, false));

        ajouterUtilisateur(new Utilisateur("mmartin", "Martin", "Marie", "marie.martin@example.com",
                "0987654321", "456 Avenue des Champs", "69002", "Lyon", passwordEncoder.encode("motdepasse2"), 200,
                false));

        ajouterUtilisateur(new Utilisateur("admin", "admin", "admin", "admin@example.com",
                "0123123123", "789 Boulevard Admin", "35000", "Rennes", passwordEncoder.encode("admin"), 1000, true));
    }

    public static Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateur.setId(nextId++);
        utilisateurs.put(utilisateur.getId(), utilisateur);
        return utilisateur;
    }

    public static List<Utilisateur> getAllUtilisateurs() {
        return new ArrayList<>(utilisateurs.values());
    }

    public static Optional<Utilisateur> getUtilisateurById(Integer id) {
        return Optional.ofNullable(utilisateurs.get(id));
    }

    public static Optional<Utilisateur> getUtilisateurByPseudo(String pseudo) {
        return utilisateurs.values().stream()
                .filter(u -> u.getPseudo().equals(pseudo))
                .findFirst();
    }

    public static Optional<Utilisateur> getUtilisateurByEmail(String email) {
        return utilisateurs.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public static boolean updateUtilisateur(Utilisateur utilisateur) {
        if (utilisateur.getId() == null || !utilisateurs.containsKey(utilisateur.getId())) {
            return false;
        }
        utilisateurs.put(utilisateur.getId(), utilisateur);
        return true;
    }

    public static boolean deleteUtilisateur(Integer id) {
        return utilisateurs.remove(id) != null;
    }

    public static Optional<Utilisateur> authentifier(String login, String motDePasse) {
        return utilisateurs.values().stream()
                .filter(u -> (u.getPseudo().equals(login) || u.getEmail().equals(login)))
                .findFirst();
    }

    public static boolean isPseudoExistant(String pseudo) {
        return utilisateurs.values().stream()
                .anyMatch(u -> u.getPseudo().equals(pseudo));
    }

    public static boolean isEmailExistant(String email) {
        return utilisateurs.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    public static void reset() {
        utilisateurs.clear();
        nextId = 1;
    }
}
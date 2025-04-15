package fr.eni.encheres.controller;

import fr.eni.encheres.service.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur nouvelUtilisateur = utilisateurService.sInscrire(utilisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelUtilisateur);
        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
    }

    @GetMapping("/profil")
    public ResponseEntity<?> getProfil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUtilisateurById(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateurConnecte.isEmpty()) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur connecté non trouvé");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erreur);
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !utilisateurConnecte.get().getId().equals(id)) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Accès non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erreur);
        }
        
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur non trouvé");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
        }
    }

    @GetMapping("/verification/pseudo/{pseudo}")
    public ResponseEntity<Map<String, Boolean>> verifierPseudo(@PathVariable String pseudo) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", !utilisateurService.isPseudoExistant(pseudo));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verification/email/{email}")
    public ResponseEntity<Map<String, Boolean>> verifierEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("disponible", !utilisateurService.isEmailExistant(email));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifierProfil(@PathVariable Integer id, @RequestBody Utilisateur utilisateur) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateurConnecte.isEmpty()) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur connecté non trouvé");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erreur);
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !utilisateurConnecte.get().getId().equals(id)) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Accès non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erreur);
        }
        
        if (!id.equals(utilisateur.getId())) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "ID incohérent");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
        
        if (!isAdmin && utilisateur.getAdministrateur() != null && utilisateur.getAdministrateur()) {
            utilisateur.setAdministrateur(false);
        }
        
        try {
            Utilisateur utilisateurMisAJour = utilisateurService.modifierProfil(utilisateur);
            return ResponseEntity.ok(utilisateurMisAJour);
        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerCompte(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateurConnecte.isEmpty()) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur connecté non trouvé");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erreur);
        }
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !utilisateurConnecte.get().getId().equals(id)) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Accès non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erreur);
        }
        
        boolean success = utilisateurService.supprimerCompte(id);
        
        if (success) {
            return ResponseEntity.noContent().build();
        } else {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Utilisateur non trouvé ou suppression impossible");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
        }
    }

    @PostMapping("/{id}/credits")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> ajouterCredits(@PathVariable Integer id, @RequestBody Map<String, Integer> body) {
        Integer montant = body.get("montant");
        
        if (montant == null) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Montant requis");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
        
        try {
            Utilisateur utilisateur = utilisateurService.ajouterCredit(id, montant);
            return ResponseEntity.ok(utilisateur);
        } catch (Exception e) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
    }
}
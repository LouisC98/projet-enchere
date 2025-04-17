package fr.eni.encheres.controller;

import fr.eni.encheres.service.UtilisateurService;
import fr.eni.encheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService, PasswordEncoder passwordEncoder) {
        this.utilisateurService = utilisateurService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Utilisateur utilisateur, 
                                @RequestParam String confirmation,
                                Model model, 
                                RedirectAttributes redirectAttributes) {
        System.out.println("Tentative d'inscription pour: " + utilisateur.getPseudo());
        
        if (!utilisateur.getMotDePasse().equals(confirmation)) {
            model.addAttribute("errorMessage", "Les mots de passe ne correspondent pas.");
            model.addAttribute("utilisateur", utilisateur);
            return "register";
        }
        
        try {
            String encodedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
            utilisateur.setMotDePasse(encodedPassword);
            
            utilisateur.setCredit(0);
            utilisateur.setAdministrateur(false);
            
            utilisateurService.sInscrire(utilisateur);
            System.out.println("Utilisateur créé avec succès: " + utilisateur.getPseudo());
            
            redirectAttributes.addFlashAttribute("successMessage", "Compte créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription: " + e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            return "register";
        }
    }

    @GetMapping("/profil")
    public String getProfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateur.isPresent()) {
            model.addAttribute("utilisateur", utilisateur.get());
            return "profil";
        } else {
            model.addAttribute("errorMessage", "Utilisateur non trouvé");
            return "error";
        }
    }

    @GetMapping("/profil/edit")
    public String editProfil(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateur.isPresent()) {
            model.addAttribute("utilisateur", utilisateur.get());
            return "edit-profil";
        } else {
            model.addAttribute("errorMessage", "Utilisateur non trouvé");
            return "error";
        }
    }

    @PostMapping("/profil/edit")
    public String updateProfil(@ModelAttribute Utilisateur utilisateur,
                              @RequestParam(required = false) String newPassword,
                              @RequestParam(required = false) String confirmation,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        
        Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateurConnecte.isEmpty()) {
            model.addAttribute("errorMessage", "Utilisateur connecté non trouvé");
            return "error";
        }
        
        Utilisateur userConnecte = utilisateurConnecte.get();
        
        if (!userConnecte.getId().equals(utilisateur.getId())) {
            model.addAttribute("errorMessage", "Vous ne pouvez pas modifier le profil d'un autre utilisateur");
            return "error";
        }
        
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmation)) {
                model.addAttribute("errorMessage", "Les mots de passe ne correspondent pas");
                model.addAttribute("utilisateur", utilisateur);
                return "edit-profil";
            }
            utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
        } else {
            utilisateur.setMotDePasse(userConnecte.getMotDePasse());
        }
        
        utilisateur.setAdministrateur(userConnecte.getAdministrateur());
        utilisateur.setCredit(userConnecte.getCredit());
        
        try {
            utilisateurService.modifierProfil(utilisateur);
            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");
            return "redirect:/profil";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("utilisateur", utilisateur);
            return "edit-profil";
        }
    }

    @PostMapping("/profil/delete")
    public String deleteAccount(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String pseudo = authentication.getName();
        
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);
        
        if (utilisateur.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur non trouvé");
            return "redirect:/";
        }
        
        boolean success = utilisateurService.supprimerCompte(utilisateur.get().getId());
        
        if (success) {
            return "redirect:/logout";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer le compte");
            return "redirect:/profil";
        }
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUsers(Model model) {
        model.addAttribute("users", utilisateurService.getAllUtilisateurs());
        return "admin/users";
    }

    @PostMapping("/admin/users/{id}/credits")
    @PreAuthorize("hasRole('ADMIN')")
    public String addCredits(@PathVariable Integer id, 
                            @RequestParam Integer montant,
                            RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.ajouterCredit(id, montant);
            redirectAttributes.addFlashAttribute("successMessage", "Crédits ajoutés avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}
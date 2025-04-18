package fr.eni.encheres.controller;

import fr.eni.encheres.service.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/register")
    public String register(Model model) {
        if (!model.containsAttribute("utilisateur")) {
            model.addAttribute("utilisateur", new Utilisateur());
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute Utilisateur utilisateur,
            @RequestParam String confirmation,
            RedirectAttributes redirectAttributes) {

        if (!utilisateur.getMotDePasse().equals(confirmation)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Les mots de passe ne correspondent pas.");
            redirectAttributes.addFlashAttribute("utilisateur", utilisateur);
            return "redirect:/register";
        }

        try {
            String encodedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
            utilisateur.setMotDePasse(encodedPassword);

            utilisateur.setCredit(0);
            utilisateur.setAdministrateur(false);

            utilisateurService.sInscrire(utilisateur);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Compte créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("Erreur lors de l'inscription: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("utilisateur", utilisateur);
            return "redirect:/register";
        }
    }

    @GetMapping("/profil")
    public String getProfil(Model model, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String pseudo = authentication.getName();

            Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);

            if (utilisateur.isPresent()) {
                model.addAttribute("utilisateur", utilisateur.get());
                return "profil/profil";
            } else {
                model.addAttribute("errorMessage", "Utilisateur non trouvé");
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Erreur lors du chargement du profil: " + e.getMessage());
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
            return "profil/edit-profil";
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
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String pseudo = authentication.getName();
            Optional<Utilisateur> utilisateurConnecte = utilisateurService.getUtilisateurByPseudo(pseudo);

            if (utilisateurConnecte.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur connecté non trouvé");
                return "redirect:/profil";
            }

            Utilisateur userConnecte = utilisateurConnecte.get();

            if (!userConnecte.getId().equals(utilisateur.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Vous ne pouvez pas modifier le profil d'un autre utilisateur");
                return "redirect:/profil";
            }

            utilisateur.setArticleVenduList(userConnecte.getArticleVenduList());

            if (newPassword != null && !newPassword.isEmpty()) {
                if (!newPassword.equals(confirmation)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Les mots de passe ne correspondent pas");
                    redirectAttributes.addFlashAttribute("utilisateur", utilisateur);
                    return "redirect:/profil/edit";
                }
                utilisateur.setMotDePasse(passwordEncoder.encode(newPassword));
            } else {
                utilisateur.setMotDePasse(userConnecte.getMotDePasse());
            }

            boolean pseudoModifie = !userConnecte.getPseudo().equals(utilisateur.getPseudo());

            utilisateurService.modifierProfil(utilisateur);

            if (pseudoModifie) {
                request.getSession().setAttribute("logoutMessage",
                        "Profil mis à jour avec succès. Veuillez vous reconnecter avec votre nouveau pseudo.");

                SecurityContextHolder.clearContext();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }

                return "redirect:/login";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Profil mis à jour avec succès");
            return "redirect:/profil";

        } catch (Exception e) {
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la mise à jour du profil: " + e.getMessage());
            return "redirect:/profil/edit";
        }
    }

    @PostMapping("/profil/delete")
    public String deleteAccount(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String pseudo = authentication.getName();

            Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurByPseudo(pseudo);

            if (utilisateur.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur non trouvé");
                return "redirect:/";
            }

            boolean success = utilisateurService.supprimerCompte(utilisateur.get().getId());

            if (success) {
                HttpSession session = request.getSession(true);
                session.setAttribute("logoutMessage", "Votre compte a été supprimé avec succès.");
                
                SecurityContextHolder.clearContext();
                
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible de supprimer le compte");
                return "redirect:/profil";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de la suppression du compte: " + e.getMessage());
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
package fr.eni.encheres.controller;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.implementation.ArticleEnchereServiceImpl;
import fr.eni.encheres.service.implementation.ArticleServiceImpl;
import fr.eni.encheres.service.implementation.CategorieServiceImpl;
import fr.eni.encheres.service.implementation.UtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private CategorieServiceImpl categorieService;

    @Autowired
    private ArticleEnchereServiceImpl articleEnchereService;
    @Autowired
    private UtilisateurServiceImpl utilisateurServiceImpl;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("articles", articleService.getAllArticles().data);
        return "article";
    }

    @PostMapping("/search")
    public String searchArticle(
            SearchCriteriaDTO criteria,
            Model model,
            Principal principal) {

        String username = principal != null ? principal.getName() : null;


        model.addAttribute("articles", articleEnchereService.advancedSearch(username,criteria).data);
        model.addAttribute("categories", categorieService.getAllCategorie().data);

        return "home";
    }

    @GetMapping("/{noArticle}")
    public String getArticleByNo(@PathVariable Long noArticle, Model model) {
        model.addAttribute("articleWithEnchere",articleEnchereService.articlesWithBestEnchere(noArticle).data);
        return "article/article";
    }

    @PostMapping("/{noArticle}/retrait")
    public String retrait(@PathVariable Long noArticle, Principal principal) {
        Optional<Utilisateur> user = utilisateurServiceImpl.getUtilisateurByPseudo(principal.getName()).data;

        user.ifPresent(utilisateur -> articleService.validerRetrait(noArticle, utilisateur));

        return "redirect:/articles/" + noArticle;
    }

    @GetMapping("/new")
    public String newArticle(Model model, Principal principal) {
        if (!model.containsAttribute("article")) {
            ArticleVendu article = new ArticleVendu();

            Retrait retrait = new Retrait();
            article.setRetrait(retrait);

            if (principal != null) {
                String username = principal.getName();
                Optional<Utilisateur> optionalUtilisateur = utilisateurServiceImpl.getUtilisateurByPseudo(username).data;

                if (optionalUtilisateur.isPresent()) {
                    Utilisateur utilisateur = optionalUtilisateur.get();

                    retrait.setRue(utilisateur.getRue());
                    retrait.setCodePostal(utilisateur.getCodePostal());
                    retrait.setVille(utilisateur.getVille());
                }
            }
            model.addAttribute("article", article);
        }
        model.addAttribute("categories", categorieService.getAllCategorie().data);
        return "article/new-article";
    }

    @PostMapping
    public String createArticle(
            @ModelAttribute ArticleVendu article,
            Principal principal,
            @RequestParam("image") MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Vérifier si une image est fournie
            if (image.isEmpty()) {
                redirectAttributes.addFlashAttribute("article", article);
                redirectAttributes.addFlashAttribute("errorMessage", "Veuillez sélectionner une image");
                return "redirect:/articles/new";
            }

            // Vérifier le type de fichier
            String contentType = image.getContentType();
            if (contentType == null ||
                    (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
                redirectAttributes.addFlashAttribute("article", article);
                redirectAttributes.addFlashAttribute("errorMessage", "Format d'image non valide. Seuls JPEG et PNG sont autorisés");
                return "redirect:/articles/new";
            }

            // Générer un nom de fichier unique
            String fileName = generateUniqueFileName(image);

            // Créer le dossier d'upload s'il n'existe pas
            Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
            Files.createDirectories(uploadDir);

            // Chemin complet du fichier
            Path filePath = uploadDir.resolve(fileName);

            // Sauvegarder le fichier
            image.transferTo(filePath.toFile());

            // Définir le nom de l'image pour l'article
            article.setImageName(fileName);

            String userName = principal.getName();
            articleService.addArticle(article, userName);

            return "redirect:/";

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("article", article);
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du téléchargement de l'image");
            return "redirect:/articles/new";
        }
    }

    private String generateUniqueFileName(MultipartFile image) {
        String originalFileName = image.getOriginalFilename();
        String extension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase()
                : "jpg";

        // Forcer l'extension à être soit jpg/jpeg, soit png
        extension = extension.matches("png|jpe?g") ? extension : "jpg";

        return UUID.randomUUID() + "." + extension;
    }
}

package fr.eni.encheres.controller;

import fr.eni.encheres.service.categorie.CategorieService;
import fr.eni.encheres.service.implementation.ArticleEnchereServiceImpl;
import fr.eni.encheres.service.implementation.CategorieServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    private CategorieServiceImpl categorieService;

    @Autowired
    private ArticleEnchereServiceImpl articleEnchereService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categories", categorieService.getAllCategorie().data);
        model.addAttribute("articles", articleEnchereService.getAllArticlesWithBestEncheres().data);
        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model,
            @RequestParam(required = false) String expired) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("logoutMessage") != null) {
            model.addAttribute("logoutMessage", session.getAttribute("logoutMessage"));
            session.removeAttribute("logoutMessage");
        }

        if (expired != null) {
            model.addAttribute("expiredMessage",
                    "Votre session a expiré pour des raisons de sécurité. Veuillez vous reconnecter.");
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}

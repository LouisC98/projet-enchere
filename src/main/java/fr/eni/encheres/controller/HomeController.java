package fr.eni.encheres.controller;

import fr.eni.encheres.service.*;
import fr.eni.encheres.service.implementation.ArticleEnchereServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private ArticleEnchereServiceImpl articleEnchereService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categories", categorieService.getCategories());
        model.addAttribute("articles", articleEnchereService.getAllArticlesWithBestEncheres().data);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}

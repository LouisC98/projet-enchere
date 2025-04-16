package fr.eni.encheres.controller;

import fr.eni.encheres.service.ArticleService;
import fr.eni.encheres.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    ArticleService articleService;

    @Autowired
    CategorieService categorieService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("categories", categorieService.getCategories());
        model.addAttribute("articles", articleService.getArticles());
        return "home";
    }
}

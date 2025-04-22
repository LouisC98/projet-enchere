package fr.eni.encheres.controller;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.categorie.CategorieService;
import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.service.implementation.ArticleEnchereServiceImpl;
import fr.eni.encheres.service.implementation.ArticleServiceImpl;
import fr.eni.encheres.service.implementation.CategorieServiceImpl;
import fr.eni.encheres.service.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private CategorieServiceImpl categorieService;

    @Autowired
    private ArticleEnchereServiceImpl articleEnchereService;

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

    @GetMapping("/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new ArticleVendu());
        model.addAttribute("categories", categorieService.getAllCategorie().data);
        return "article/new-article";
    }

    @PostMapping
    public String createArticle(@ModelAttribute ArticleVendu article,Principal principal) {

        String userName = principal.getName();
        articleService.addArticle(article, userName);
        return "redirect:/";
    }
}

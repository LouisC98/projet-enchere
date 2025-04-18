package fr.eni.encheres.controller;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.CategorieService;
import fr.eni.encheres.service.implementation.ArticleService;
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
    private ArticleService articleService;

    @Autowired
    private CategorieService categorieService;



    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("articles", articleService.getAllArticles().data);
        return "article";
    }

    @PostMapping("/search")
    public String searchArticle(
            @RequestParam(required = false) Long noCategorie,
            @RequestParam(required = false) String searchName,
            Model model) {


        model.addAttribute("articles", articleService.SearchArticlesVendu(noCategorie, searchName).data);
        model.addAttribute("categories", categorieService.getCategories());

        return "home";
    }

    @GetMapping("/{noArticle}")
    public String getArticleByNo(@PathVariable Long noArticle, Model model) {
        model.addAttribute("article", articleService.getArticleById(noArticle));
        return "article/article";
    }

    @GetMapping("/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new ArticleVendu());
        model.addAttribute("categories", categorieService.getCategories());
        return "article/new-article";
    }

    @PostMapping
    public String createArticle(@ModelAttribute ArticleVendu article,Principal principal) {

        String userName = principal.getName();
        articleService.addArticle(article, userName);
        return "redirect:/";
    }
}

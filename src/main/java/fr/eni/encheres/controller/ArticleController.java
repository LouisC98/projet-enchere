package fr.eni.encheres.controller;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.ArticleService;
import fr.eni.encheres.service.CategorieService;
import fr.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        model.addAttribute("articles",articleService.getArticles());
        return "article";
    }

    @PostMapping("/search")
    public String searchArticle(
            @RequestParam(required = false) Long noCategorie,
            @RequestParam(required = false) String searchName,
            Model model) {

        List<ArticleVendu> filteredArticles = articleService.searchArticles(noCategorie, searchName);

        model.addAttribute("articles", filteredArticles);
        model.addAttribute("categories", categorieService.getCategories());

        return "home";
    }

    @GetMapping("/{noArticle}")
    public String getArticleByNo(@PathVariable Long noArticle, Model model) {
        model.addAttribute("article",articleService.getArticle(noArticle));
        return "article/article";
    }

    @GetMapping("/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new ArticleVendu());
        model.addAttribute("categories", categorieService.getCategories());
        return "article/new-article";
    }

    @PostMapping
    public String createArticle(@ModelAttribute ArticleVendu article) {
        articleService.creerArticle(article);
        return "redirect:/";
    }
}

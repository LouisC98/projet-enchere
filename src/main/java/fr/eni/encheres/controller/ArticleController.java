package fr.eni.encheres.controller;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.ArticleService;
import fr.eni.encheres.service.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("articles",articleService.getArticles());
        return "article";
    }

    @GetMapping("/{noArticle}")
    public String getArticleByNo(@PathVariable Long noArticle, Model model) {
        model.addAttribute("article",articleService.getArticle(noArticle));
        System.out.println(articleService.getArticle(noArticle));
        return "article/article";
    }

    @PostMapping
    public void createArticle(ArticleVendu article) {
        articleService.creerArticle(article);
    }
}

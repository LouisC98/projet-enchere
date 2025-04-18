package fr.eni.encheres.controller;


import fr.eni.encheres.service.ArticleEnchereService;
import fr.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/encheres")
public class EnchereController {

    @Autowired
    private ArticleEnchereService articleEnchereService;

    @Autowired
    private EnchereService enchereService;

    @GetMapping()
    public void getEncheres(Model model) {
//        model.addAttribute("encheres",service.getEncheres());
        System.out.println(enchereService.getEncheres());
    };

    @GetMapping("/{name}")
    public String getEnchere(@PathVariable String name, Model model) {
        model.addAttribute("encheres",enchereService.getEnchere(name));
        return "article/article";
    }

    @PostMapping("/{noArticle}")
    public String creerEnchere(@PathVariable Long noArticle, @RequestParam int propal, Principal principal) {
        String userName = principal.getName();
        articleEnchereService.addEnchere(userName, noArticle,propal);
        return "redirect:/articles/"+noArticle;
    }
}

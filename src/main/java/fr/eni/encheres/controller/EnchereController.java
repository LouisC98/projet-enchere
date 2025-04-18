package fr.eni.encheres.controller;


import fr.eni.encheres.service.implementation.ArticleEnchereServiceImpl;
import fr.eni.encheres.service.implementation.EnchereServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/encheres")
public class EnchereController {

    @Autowired
    private ArticleEnchereServiceImpl articleEnchereService;

    @Autowired
    private EnchereServiceImpl enchereServiceImpl;

    @GetMapping()
    public void getEncheres(Model model) {
//        model.addAttribute("encheres",service.getEncheres());
        System.out.println(enchereServiceImpl.getAllEncheres());
    };

//    @GetMapping("/{name}")
//    public String getEnchere(@PathVariable String name, Model model) {
//        model.addAttribute("encheres", enchereService.getEncheresByNoArticle(name));
//        return "article/article";
//    }

    @PostMapping("/{noArticle}")
    public String creerEnchere(@PathVariable Long noArticle, @RequestParam int propal, Principal principal) {
        String userName = principal.getName();
        articleEnchereService.addEnchere(userName, noArticle,propal);
        return "redirect:/articles/"+noArticle;
    }
}

package fr.eni.encheres.controller;


import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.EnchereService;
import fr.eni.encheres.service.implementation.EnchereServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class EnchereController {

    @Autowired
    private EnchereServiceImpl service;

    public EnchereController(EnchereServiceImpl service) {
        this.service = service;
    }

    @GetMapping()
    public String getEncheres(Model model) {
        model.addAttribute("encheres",service.getEncheres());
        return "home";
    };

    @GetMapping("/{name}")
    public String getEnchere(@PathVariable String name, Model model) {
        model.addAttribute("encheres",service.getEnchere(name));
        return "enchere/enchere";
    }



    @PostMapping("/enchere")
    public void creerEnchere(ArticleVendu articleVendu, Model model) {
        service.addEnchere(articleVendu);
    }


}

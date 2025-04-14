package fr.eni.encheres.controller;


import fr.eni.encheres.service.EnchereService;
import fr.eni.encheres.service.implementation.EnchereServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EnchereController {

    private EnchereServiceImpl service;

    public EnchereController(EnchereServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/enchere")
    public String enchere(Model model) {
        model.addAttribute("enchere",service.getEncheres());
        return "enchere";
    };


}

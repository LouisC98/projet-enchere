package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.ArticleService;
import fr.eni.encheres.service.EnchereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class EnchereServiceImpl implements EnchereService {


    private static List<Enchere> encheres = new ArrayList<>();

    @Autowired
    private ArticleService articleService;


    public EnchereServiceImpl(ArticleService articleService) {
        this.articleService = articleService;
        mockEnchere();
    }

    public void mockEnchere(){

        Enchere enchere1 = new Enchere(LocalDateTime.now());
        Enchere enchere2 = new Enchere(LocalDateTime.now());

        enchere1.setMontantEnchere(articleService.getArticles().get(0).getMisAPrix());
        enchere2.setMontantEnchere(articleService.getArticles().get(1).getMisAPrix());
        enchere1.setArticleVendu(articleService.getArticles().get(0));
        enchere2.setArticleVendu(articleService.getArticles().get(1));
        System.out.println(articleService.getArticles().get(1).getMisAPrix());
        encheres.add(enchere1);
        encheres.add(enchere2);


    }


    /***
     * Retourne la liste des enchères.
     * Rajouter une logique de date pour retourner que celle en cours
     * @return
     */
    @Override
    public List<Enchere> getEncheres() {
        return encheres;
    }


    /**
     * Retourne une liste d'encheres en fonction du nom de l'article vendus
     *
     * @param name
     * @return
     */
    @Override
    public Enchere getEnchere(String name) {
        return encheres.stream().filter(enchere -> enchere.getArticleVendu().getNomArticle().equals(name)).findFirst().orElse(null);
    }



    /**
     * Création d'une enchère en prenant la date du jour et l'article lié.
     *
     * @param articleVendu
     */
    @Override
    public void addEnchere(ArticleVendu articleVendu) {
        Enchere enchere = new Enchere(LocalDateTime.now(),articleVendu.getMisAPrix(),articleVendu);
        encheres.add(enchere);
    }

    @Override
    public Enchere getEnchereById(String id) {
        return encheres.stream().filter(e -> e.getArticleVendu().getNoArticle().equals(id)).findFirst().orElse(null);
    }


}

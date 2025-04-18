package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.bo.Retrait;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.CategorieService;
import fr.eni.encheres.service.UtilisateurService;
import fr.eni.encheres.service.article.ArticleServiceInterface;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ArticleMock implements ArticleServiceInterface {

    private List<ArticleVendu> articles = new ArrayList<>();
    private static int INDEX = 2;

    @Autowired
    CategorieService categorieService;

    @Autowired
    UtilisateurService utilisateurService;

    @PostConstruct
    public void init() {
        mockArticles();
    }

    @Override
    public void creerArticle(ArticleVendu article, String userName) {
        INDEX++;
        article.setNoArticle((long) INDEX);
        article.setCategorie(categorieService.getCategorie(article.getCategorie().getNoCategorie()));
        article.getRetrait().setArticleVendu(Optional.of(article));

        Utilisateur user = utilisateurService.getUtilisateurByPseudo(userName).orElseThrow();
        article.setVendeur(user);
        utilisateurService.addArticleAVendre(user, article);

        articles.add(article);
    }

    @Override
    public List<ArticleVendu> getArticles() {
        return articles;
    }

    @Override
    public ArticleVendu getArticle(Long noArticle) {
        return articles.stream()
                .filter(article -> article.getNoArticle().equals(noArticle))
                .findFirst()
                .orElse(null);
    }

    /**
     * Mock des articles
     * Ajouter les adresses par défaut de l'acheteur.
     */
    public void mockArticles(){
        // Premier article: Fauteuil
        ArticleVendu fauteuil = new ArticleVendu();
        fauteuil.setNoArticle(123L);
        fauteuil.setNomArticle("Fauteuil");
        fauteuil.setDescription("Fauteuil en cuir");
        fauteuil.setCategorie(categorieService.getCategories().get(0));
        fauteuil.setMisAPrix(310);
        fauteuil.setDateDebutEncheres(LocalDateTime.of(2018, 8, 10, 14, 45));
        fauteuil.setDateFinEnchere(LocalDateTime.of(2022, 12, 1, 18, 15));
        fauteuil.setVendeur(utilisateurService.getUtilisateurById(1).orElseThrow());

        Retrait retraitFauteuil = new Retrait();
        retraitFauteuil.setRue("test");
        retraitFauteuil.setCodePostal(48);
        retraitFauteuil.setVille("Niort");
        fauteuil.setRetrait(retraitFauteuil);

        // Deuxième article: PC Gamer
        ArticleVendu pc = new ArticleVendu();
        pc.setNoArticle(2L);
        pc.setNomArticle("PC Gamer");
        pc.setDescription("Un PC Gamer haute performance avec une carte graphique de dernière génération");
        pc.setCategorie(categorieService.getCategories().get(1));
        pc.setMisAPrix(1000);
        pc.setDateDebutEncheres(LocalDateTime.of(2025, 4, 15, 10, 30));
        pc.setDateFinEnchere(LocalDateTime.of(2030, 6, 5, 7, 0));
        pc.setVendeur(utilisateurService.getUtilisateurById(2).orElseThrow());

        Retrait retraitPc = new Retrait();
        retraitPc.setRue("test 18 rue");
        retraitPc.setCodePostal(87);
        retraitPc.setVille("La Rochelle");
        pc.setRetrait(retraitPc);

        articles.add(fauteuil);
        articles.add(pc);
    }
}
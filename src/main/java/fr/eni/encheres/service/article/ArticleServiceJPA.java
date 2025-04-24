package fr.eni.encheres.service.article;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.EtatVente;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.user.UtilisateurService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Profile("Prod")
@Repository
public class ArticleServiceJPA implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public void creerArticle(ArticleVendu article, String username) {
        Optional<Utilisateur> vendeur = utilisateurService.getUtilisateurByPseudo(username);
        vendeur.ifPresent(article::setVendeur);
        article.setEtatVente(EtatVente.CREEE);
        article.getRetrait().setArticleVendu(article);

        articleRepository.save(article);
    }

    @Override
    public List<ArticleVendu> getArticles() {
        return articleRepository.findAll();
    }

    @Override
    public ArticleVendu getArticle(Long noArticle) {
        return articleRepository.findById(noArticle).orElse(null);
    }

    @Override
    public ArticleVendu updateEtatVente(ArticleVendu article) {
        if (article == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        // Ne pas changer l'état des articles déjà finalisés
        if (article.getEtatVente() == EtatVente.VENDU ||
                article.getEtatVente() == EtatVente.NON_VENDU ||
                article.getEtatVente() == EtatVente.RETRAIT_EFFECTUE) {
            return article;
        }

        // Déterminer le nouvel état selon les dates
        if (now.isBefore(article.getDateDebutEncheres())) {
            article.setEtatVente(EtatVente.CREEE);
        } else if (now.isAfter(article.getDateDebutEncheres()) &&
                now.isBefore(article.getDateFinEnchere())) {
            article.setEtatVente(EtatVente.EN_COURS);
        } else if (now.isAfter(article.getDateFinEnchere())) {
            article.setEtatVente(EtatVente.TERMINEE);
        }

        // Sauvegarder les modifications
        return articleRepository.save(article);
    }
}
package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.article.ArticleServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Component
public class EnchereMockInterface implements EnchereServiceInterface {


    private static List<Enchere> encheres = new ArrayList<>();

    @Autowired
    private ArticleServiceInterface articleServiceInterface;


    public EnchereMockInterface(ArticleServiceInterface articleServiceInterface) {
        this.articleServiceInterface = articleServiceInterface;
        mockEnchere();
    }

    public void mockEnchere(){

        Enchere enchere1 = new Enchere(LocalDateTime.now());
        Enchere enchere2 = new Enchere(LocalDateTime.now());
        enchere1.setMontantEnchere(articleServiceInterface.getArticles().get(0).getMisAPrix());
        enchere2.setMontantEnchere(articleServiceInterface.getArticles().get(1).getMisAPrix());
        enchere1.setArticleVendu(articleServiceInterface.getArticles().get(0));
        enchere2.setArticleVendu(articleServiceInterface.getArticles().get(1));
        System.out.println(articleServiceInterface.getArticles().get(1).getMisAPrix());
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


    @Override
    public List<Enchere> getEncheresByNoArticle(Long noArticle) {
        return encheres.stream().filter(e -> e.getArticleVendu().getNoArticle().equals(noArticle)).toList();
    }

    @Override
    public void addEnchere(Long noArticle, int propal) {
        ArticleVendu article = articleServiceInterface.getArticle(noArticle);
        List<Enchere> encheresByArticle = getEncheresByNoArticle(noArticle);

        Optional<Enchere> maxEnchere = encheresByArticle.stream()
                .max(Comparator.comparingInt(Enchere::getMontantEnchere));

        if (maxEnchere.isEmpty()) {
            if (propal > article.getMisAPrix()) {
                Enchere nouvelleEnchere = new Enchere(LocalDateTime.now(), propal, article);
                encheres.add(nouvelleEnchere);
            }
        }
        else if (propal > maxEnchere.get().getMontantEnchere()) {
            Enchere nouvelleEnchere = new Enchere(LocalDateTime.now(), propal, article);
            encheres.add(nouvelleEnchere);
        }
    }
}

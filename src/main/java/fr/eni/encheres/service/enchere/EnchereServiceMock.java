package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EnchereServiceMock implements EnchereService {
    private static List<Enchere> encheres = new ArrayList<>();

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
     * Retourne une enchère en fonction du nom de l'article vendu
     *
     * @param name
     * @return
     */
    @Override
    public Enchere getEnchere(String name) {
        return encheres.stream()
                .filter(enchere -> enchere.getArticleVendu().getNomArticle().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Enchere> getEncheresByNoArticle(Long noArticle) {
        return encheres.stream()
                .filter(e -> e.getArticleVendu().getNoArticle().equals(noArticle))
                .toList();
    }

    @Override
    public Enchere getMaxEnchereByNoArticle(Long noArticle) {
        return encheres.stream()
                .filter(e -> e.getArticleVendu().getNoArticle().equals(noArticle))
                .max(Comparator.comparingInt(Enchere::getMontantEnchere))
                .orElse(null);
    }

    @Override
    public void addEnchere(Enchere enchere) {
        encheres.add(enchere);
    }
}
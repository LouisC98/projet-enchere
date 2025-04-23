package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Profile("Dev")
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

    @Override
    public void updateEnchere(Enchere enchere) {
        // Trouver l'indice de l'enchère à mettre à jour
        for (int i = 0; i < encheres.size(); i++) {
            Enchere existingEnchere = encheres.get(i);
            // Si même article et même enchérisseur
            if (existingEnchere.getArticleVendu().getNoArticle().equals(enchere.getArticleVendu().getNoArticle()) &&
                    existingEnchere.getEncherisseur().getId().equals(enchere.getEncherisseur().getId())) {
                // Remplacer l'ancienne enchère par la nouvelle
                encheres.set(i, enchere);
                return;
            }
        }
        // Si l'enchère n'existe pas encore, l'ajouter
        encheres.add(enchere);
    }
}
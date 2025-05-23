package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> getEncheres();

    List<Enchere> getEncheresByNoArticle(Long noArticle);

    Enchere getMaxEnchereByNoArticle(Long noArticle);

    void addEnchere(Enchere enchere);

    void updateEnchere(Enchere enchere);
}
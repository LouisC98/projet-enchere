package fr.eni.encheres.service;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> getEncheres();

    Enchere getEnchere(String name);

    List<Enchere> getEncheresByNoArticle(Long noArticle);

    Enchere getMaxEnchereByNoArticle(Long noArticle);

    void addEnchere(Enchere enchere);

}

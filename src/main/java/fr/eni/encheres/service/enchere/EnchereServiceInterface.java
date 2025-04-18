package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface EnchereServiceInterface {

    public List<Enchere> getEncheres();

    public void addEnchere(Long noArticle, int prix);

    public List<Enchere> getEncheresByNoArticle(Long noArticle);

    public Enchere getMaxEnchereByNoArticle(Long noArticle);
}

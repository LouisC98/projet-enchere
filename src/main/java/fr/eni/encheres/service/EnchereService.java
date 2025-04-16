package fr.eni.encheres.service;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;

import java.util.List;

public interface EnchereService {

    public List<Enchere> getEncheres();

    public Enchere getEnchere(String name);

    public void addEnchere(ArticleVendu articleVendu);

    public Enchere getEnchereById(Long id);

    public void encherir(Long id, int prix);


}

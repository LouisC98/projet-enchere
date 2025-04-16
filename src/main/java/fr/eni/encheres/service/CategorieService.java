package fr.eni.encheres.service;

import fr.eni.encheres.bo.Categorie;

import java.util.List;

public interface CategorieService {

    public List<Categorie> getCategories();

    public Categorie getCategorie(Long noCategorie);
}

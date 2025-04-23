package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class EnchereServiceJPA implements EnchereService{

    private EnchereRepository enchereRepository;

    @Override
    public List<Enchere> getEncheres() {
        return List.of();
    }

    @Override
    public Enchere getEnchere(String name) {
        return null;
    }

    @Override
    public List<Enchere> getEncheresByNoArticle(Long noArticle) {
        return List.of();
    }

    @Override
    public Enchere getMaxEnchereByNoArticle(Long noArticle) {
        return null;
    }

    @Override
    public void addEnchere(Enchere enchere) {

    }

    @Override
    public void updateEnchere(Enchere enchere) {

    }
}

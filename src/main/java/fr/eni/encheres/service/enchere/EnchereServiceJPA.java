package fr.eni.encheres.service.enchere;

import fr.eni.encheres.bo.Enchere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Profile("Prod")
@Repository
public class EnchereServiceJPA implements EnchereService{

    @Autowired
    private EnchereRepository enchereRepository;

    @Override
    public List<Enchere> getEncheres() {
        return enchereRepository.findAll();
    }

    @Override
    public List<Enchere> getEncheresByNoArticle(Long noArticle) {
        return enchereRepository.findByArticleVenduNoArticle(noArticle);
    }

    @Override
    public Enchere getMaxEnchereByNoArticle(Long noArticle) {
        return enchereRepository.findMaxEnchereByNoArticle(noArticle);
    }

    @Override
    public void addEnchere(Enchere enchere) {
        enchereRepository.save(enchere);
    }

    @Override
    public void updateEnchere(Enchere enchere) {
        enchereRepository.save(enchere);
    }
}
package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.EnchereService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class EnchereServiceImpl implements EnchereService {


    private static List<Enchere> encheres = new ArrayList<>();


    public EnchereServiceImpl() {

    }

    public void mockEnchere(){
        ArticleVendu fauteuil = new ArticleVendu(
                "123",
                "Fauteil",
                "Fauteil en cuir",
                new Date(2018, 8, 10),
                new Date(2018, 1,9),
                310,
                400,
                "En cours"
        );

        ArticleVendu pc = new ArticleVendu(
                "002",
                "PC Gamer",
                "Un PC Gamer haute performance avec une carte graphique de dernière génération",
                new Date(2025, 4, 20),
                new Date(2018, 10, 9),
                1000,
                1444,
                "Vendu"
        );

        Enchere enchere1 = new Enchere(new Date(2025, 4, 20),1000,pc);
        Enchere enchere2 = new Enchere(new Date(2018, 8, 10),1000,fauteuil);

        encheres.add(enchere1);
        encheres.add(enchere2);
    }


    @Override
    public List<Enchere> getEncheres() {
        return encheres;
    }

    @Override
    public Enchere getEnchere(String name) {
        return null;
    }

    @Override
    public void addEnchere(Enchere enchere) {

    }
}

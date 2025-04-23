package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Enchere;
import fr.eni.encheres.service.enchere.EnchereService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class EnchereServiceImplTest {

    @InjectMocks
    private EnchereServiceImpl enchereServiceImpl;

    @Mock
    private EnchereService enchereService;

    @Mock
    private ArticleServiceImpl articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEncheres_success() {

        List<Enchere> mockEncheres = new ArrayList<>();
        mockEncheres.add(new Enchere());
        when(enchereService.getEncheres()).thenReturn(mockEncheres);


        ServiceResponse<List<Enchere>> response = enchereServiceImpl.getAllEncheres();


        assertEquals("La liste de toutes les enchères a bien été récupérée", response.message);
        assertEquals(mockEncheres, response.data);
    }

    @Test
    void testGetAllEncheres_notFound() {

        when(enchereService.getEncheres()).thenReturn(new ArrayList<>());


        ServiceResponse<List<Enchere>> response = enchereServiceImpl.getAllEncheres();


        assertEquals("Aucune enchère existante", response.message);
        assertTrue(response.data.isEmpty());
    }


    @Test
    void testGetMaxEnchere_notFound() {

        Long noArticle = 1L;
        when(enchereService.getMaxEnchereByNoArticle(noArticle)).thenReturn(null);


        ServiceResponse<Enchere> response = enchereServiceImpl.getMaxEnchere(noArticle);


        assertEquals("Aucune enchere sur 1 a été trouvé", response.message);
        assertNull(response.data);
    }

    @Test
    void testGetMaxEnchere_success() {

        Enchere mockEnchere = new Enchere();
        ArticleVendu mockArticleVendu = new ArticleVendu();
        mockArticleVendu.setNomArticle("Article Test");
        mockEnchere.setArticleVendu(mockArticleVendu);

        when(enchereService.getMaxEnchereByNoArticle(1L)).thenReturn(mockEnchere);


        ServiceResponse<Enchere> response = enchereServiceImpl.getMaxEnchere(1L);


        assertEquals("L'enchere Article Test est existante", response.message);
        assertEquals(mockEnchere, response.data);
    }
}
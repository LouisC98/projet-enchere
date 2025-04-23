package fr.eni.encheres.service.implementation;

import fr.eni.encheres.dto.ArticleWithBestEnchereDTO;
import fr.eni.encheres.dto.SearchCriteriaDTO;
import fr.eni.encheres.service.articleEnchere.ArticleEnchereService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ArticleEnchereServiceImplTest {

    @Mock
    private ArticleEnchereService articleEnchereService;

    @InjectMocks
    private ArticleEnchereServiceImpl articleEnchereServiceImpl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllArticlesWithBestEncheres_EmptyList() {
        when(articleEnchereService.getArticlesWithBestEncheres()).thenReturn(null);

        ServiceResponse<List<ArticleWithBestEnchereDTO>> response = articleEnchereServiceImpl.getAllArticlesWithBestEncheres();


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Liste des meilleures encheres vide", response.message);
        assertNull(response.data);

    }

    @Test
    public void testGetAllArticlesWithBestEncheres_Success() {

        List<ArticleWithBestEnchereDTO> bestEncheres = List.of(new ArticleWithBestEnchereDTO(), new ArticleWithBestEnchereDTO());
        when(articleEnchereService.getArticlesWithBestEncheres()).thenReturn(bestEncheres);

        ServiceResponse<List<ArticleWithBestEnchereDTO>> response = articleEnchereServiceImpl.getAllArticlesWithBestEncheres();

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Liste des meilleures encheres récupérées", response.message);
        assertEquals(bestEncheres, response.data);

    }

    @Test
    public void testSearchArticlesWithBestEnchere_Success() {

        List<ArticleWithBestEnchereDTO> searchResults = List.of(new ArticleWithBestEnchereDTO());
        Long noCategorie = 1L;
        String searchName = "Test";
        when(articleEnchereService.searchArticles(noCategorie, searchName)).thenReturn(searchResults);

        ServiceResponse<List<ArticleWithBestEnchereDTO>> response = articleEnchereServiceImpl.searchArticlesWithBestEnchere(noCategorie, searchName);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Liste des meilleures encheres récupérées selon critères", response.message);
        assertEquals(searchResults, response.data);

    }

    @Test
    public void testArticlesWithBestEnchere_NotFound() {

        Long noArticle = 1L;
        when(articleEnchereService.getArticleWithBestEnchere(noArticle)).thenReturn(null);

        ServiceResponse<ArticleWithBestEnchereDTO> response = articleEnchereServiceImpl.articlesWithBestEnchere(noArticle);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Meilleure enchere sur l'article non existante", response.message);
        assertNull(response.data);

    }

    @Test
    public void testArticlesWithBestEnchere_Success() {

        Long noArticle = 1L;
        ArticleWithBestEnchereDTO bestEnchere = new ArticleWithBestEnchereDTO();
        when(articleEnchereService.getArticleWithBestEnchere(noArticle)).thenReturn(bestEnchere);

        ServiceResponse<ArticleWithBestEnchereDTO> response = articleEnchereServiceImpl.articlesWithBestEnchere(noArticle);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Meilleure enchere sur l'article " + noArticle + " récupérée", response.message);
        assertEquals(bestEnchere, response.data);

    }

    @Test
    public void testAddEnchere_Success() {

        String userName = "TestUser";
        Long noArticle = 1L;
        int propal = 100;
        doNothing().when(articleEnchereService).addEnchere(userName, noArticle, propal);


        ServiceResponse<String> response = articleEnchereServiceImpl.addEnchere(userName, noArticle, propal);


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("L'enchere a été créé", response.message);
        assertEquals("ok", response.data);

    }

    @Test
    public void testAdvancedSearch_Success() {

        String username = "TestUser";
        SearchCriteriaDTO criteria = new SearchCriteriaDTO();
        List<ArticleWithBestEnchereDTO> advancedSearchResults = List.of(new ArticleWithBestEnchereDTO());
        when(articleEnchereService.advancedSearch(username, criteria)).thenReturn(advancedSearchResults);

        ServiceResponse<List<ArticleWithBestEnchereDTO>> response = articleEnchereServiceImpl.advancedSearch(username, criteria);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Recherche avancée effectuée avec succès", response.message);
        assertEquals(advancedSearchResults, response.data);

    }
}
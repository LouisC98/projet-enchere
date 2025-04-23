package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.service.article.ArticleService;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ArticleServiceImplTest {
    
        @Mock
        private ArticleService articleService;

        @InjectMocks
        private ArticleServiceImpl articleServiceImpl;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testGetAllArticles_EmptyList() {

        when(articleService.getArticles()).thenReturn(Collections.emptyList());

        ServiceResponse<List<ArticleVendu>> response = articleServiceImpl.getAllArticles();

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Liste vide", response.message);
        assertNull(response.data);
        
    }

        @Test
        public void testGetAllArticles_Success() {

        List<ArticleVendu> articles = Arrays.asList(new ArticleVendu(), new ArticleVendu());
        when(articleService.getArticles()).thenReturn(articles);

        ServiceResponse<List<ArticleVendu>> response = articleServiceImpl.getAllArticles();

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("La liste des articles a été récupéré", response.message);
        assertEquals(articles, response.data);

    }

        @Test
        public void testGetArticleById_NotFound() {

        when(articleService.getArticle(1L)).thenReturn(null);


        ServiceResponse<ArticleVendu> response = articleServiceImpl.getArticleById(1L);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Aucun article correspondant", response.message);
        assertNull(response.data);

    }

        @Test
        public void testGetArticleById_Success() {

        ArticleVendu article = new ArticleVendu();
        when(articleService.getArticle(1L)).thenReturn(article);


        ServiceResponse<ArticleVendu> response = articleServiceImpl.getArticleById(1L);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Article 1 récupéré", response.message);
        assertEquals(article, response.data);

    }

        @Test
        public void testAddArticle_Success() {

        ArticleVendu article = new ArticleVendu();
        article.setNomArticle("Test Article");
        String username = "TestUser";

        doNothing().when(articleService).creerArticle(article, username);

        ServiceResponse<String> response = articleServiceImpl.addArticle(article, username);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Article créé par : TestUser", response.message);
        assertEquals("Test Article", response.data);

    }

        @Test
        public void testUpdateEtatVente_NotFound() {

        when(articleService.getArticle(1L)).thenReturn(null);


        ServiceResponse<ArticleVendu> response = articleServiceImpl.updateEtatVente(1L);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Article non trouvé", response.message);
        assertNull(response.data);

    }

        @Test
        public void testUpdateEtatVente_Success() {

        ArticleVendu article = new ArticleVendu();
        when(articleService.getArticle(1L)).thenReturn(article);
        when(articleService.updateEtatVente(article)).thenReturn(article);


        ServiceResponse<ArticleVendu> response = articleServiceImpl.updateEtatVente(1L);


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("État de l'article mis à jour avec succès", response.message);
        assertEquals(article, response.data);

    }
}
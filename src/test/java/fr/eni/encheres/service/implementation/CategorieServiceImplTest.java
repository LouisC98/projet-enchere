package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.Categorie;
import fr.eni.encheres.service.categorie.CategorieService;
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

class CategorieServiceImplTest {

    @Mock
    private CategorieService categorieService;

    @InjectMocks
    private CategorieServiceImpl categorieServiceImpl; 

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCategorie_NotFound() {

        Long noCategorie = 1L;
        when(categorieService.getCategorie(noCategorie)).thenReturn(null);


        ServiceResponse<Categorie> response = categorieServiceImpl.getCategorie(noCategorie);


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Aucune catégorie correspondante à l'id " + noCategorie, response.message);
        assertNull(response.data);
  
    }

    @Test
    public void testGetCategorie_Success() {

        Long noCategorie = 1L;
        Categorie categorie = new Categorie();
        categorie.setLibelle("Test Category");
        when(categorieService.getCategorie(noCategorie)).thenReturn(categorie);


        ServiceResponse<Categorie> response = categorieServiceImpl.getCategorie(noCategorie);


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("La catégorie correspondant à l'id " + noCategorie + "(Test Category) a été trouvé", response.message);
        assertEquals(categorie, response.data);
        
    }

    @Test
    public void testGetAllCategorie_EmptyList() {

        when(categorieService.getCategories()).thenReturn(null);


        ServiceResponse<List<Categorie>> response = categorieServiceImpl.getAllCategorie();


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("Aucune catégorie n'a été créé", response.message);
        assertNull(response.data);
  
    }

    @Test
    public void testGetAllCategorie_Success() {

        List<Categorie> categorieList = Arrays.asList(new Categorie(), new Categorie());
        when(categorieService.getCategories()).thenReturn(categorieList);


        ServiceResponse<List<Categorie>> response = categorieServiceImpl.getAllCategorie();


        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("La liste des catégories a été récupéré", response.message);
        assertEquals(categorieList, response.data);
      
    }

    @Test
    void testAddCategorie_CategorieAlreadyExists() {
        Categorie categorie = new Categorie(789L,"TestLibelle");
        List<Categorie> categorieList = List.of(categorie);

        when(categorieService.getCategories()).thenReturn(categorieList);

        ServiceResponse<Categorie> response = categorieServiceImpl.addCategorie(categorie);

        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("La catégorie existe déjà", response.message);
    }

    @Test
    void testAddCategorie_CategorieAddedSuccessfully() {
        Categorie categorie = new Categorie(789L,"NouvelleCategorie");

        when(categorieService.getCategories()).thenReturn(List.of());
        doNothing().when(categorieService).addCategorie(categorie);

        ServiceResponse<Categorie> response = categorieServiceImpl.addCategorie(categorie);

        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("La catégorie a été créé", response.message);
        assertEquals(categorie, response.data);
    }
}
package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import fr.eni.encheres.service.user.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UtilisateurServiceImplTest {

    

        @Mock
        private UtilisateurService utilisateurService;

        @InjectMocks
        private UtilisateurServiceImpl utilisateurServiceImpl;

        @BeforeEach
        public void setup() {
        MockitoAnnotations.openMocks(this);
    }

        @Test
        public void testSeConnecter_Success() {

        String pseudo = "TestUser";
        String motDePasse = "password";
        Utilisateur user = new Utilisateur();
        when(utilisateurService.authentifier(pseudo, motDePasse)).thenReturn(Optional.of(user));

        Optional<Utilisateur> response = utilisateurServiceImpl.seConnecter(pseudo, motDePasse);

        assertTrue(response.isPresent());
        assertEquals(user, response.get());

    }

        @Test
        public void testSInscrire_PseudoExistant() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("ExistingUser");
        utilisateur.setEmail("email@example.com");
        when(utilisateurService.isPseudoExistant("ExistingUser")).thenReturn(true);

        ServiceResponse<Utilisateur> response = utilisateurServiceImpl.sInscrire(utilisateur);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_TCH, response.code);
        assertEquals("Ce pseudo est déjà utilisé.", response.message);

    }

        @Test
        public void testModifierProfil_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("UpdatedUser");
        when(utilisateurService.updateUtilisateur(utilisateur)).thenReturn(true);

        ServiceResponse<Utilisateur> response = utilisateurServiceImpl.modifierProfil(utilisateur);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("L'utilisateur UpdatedUser a été modifié.", response.message);

    }

        @Test
        public void testSupprimerCompte_Success() {

        Integer userId = 1;
        when(utilisateurService.deleteUtilisateur(userId)).thenReturn(true);

        boolean result = utilisateurServiceImpl.supprimerCompte(userId);

        assertTrue(result);

    }

        @Test
        public void testGetUtilisateurById_NotFound() {
        Integer userId = 1;
        when(utilisateurService.getUtilisateurById(userId)).thenReturn(Optional.empty());

        ServiceResponse<Optional<Utilisateur>> response = utilisateurServiceImpl.getUtilisateurById(userId);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_NOT_FOUND, response.code);
        assertEquals("L'utilisateur est introuvable", response.message);
        assertNull(response.data);

    }

        @Test
        public void testAjouterCredit_Success() throws Exception {

        Integer userId = 1;
        Integer montant = 100;
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setPseudo("TestUser");
        utilisateur.setCredit(300);
        when(utilisateurService.getUtilisateurById(userId)).thenReturn(Optional.of(utilisateur));
        when(utilisateurService.updateUtilisateur(utilisateur)).thenReturn(true);

        ServiceResponse<Utilisateur> response = utilisateurServiceImpl.ajouterCredit(userId, montant);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Le crédit a été ajouté à'utilisateur TestUser", response.message);
        assertEquals(300, utilisateur.getCredit());

    }

        @Test
        public void testAddCredits_Success() {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setCredit(200);
        when(utilisateurService.getUtilisateurById(utilisateur.getId())).thenReturn(Optional.of(utilisateur));

        ServiceResponse<String> response = utilisateurServiceImpl.addCredits(utilisateur, 100);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_SUCCESS, response.code);
        assertEquals("Le crédit a bien été ajouté", response.message);
        assertEquals(300, utilisateur.getCredit());

    }

        @Test
        public void testRemoveCredits_NotEnoughCredit() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1);
        utilisateur.setCredit(50);
        when(utilisateurService.getUtilisateurById(utilisateur.getId())).thenReturn(Optional.of(utilisateur));

        ServiceResponse<String> response = utilisateurServiceImpl.removeCredits(utilisateur, 100);

        assertNotNull(response);
        assertEquals(ServiceConstant.CD_ERR_TCH, response.code);
        assertEquals("L'utilisateur n'a pas assez de crédit", response.message);
        assertEquals(50, utilisateur.getCredit());

    }
}
package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import fr.eni.encheres.service.user.UtilisateurMock;
import fr.eni.encheres.service.user.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl {


    @Autowired
    UtilisateurService utilisateurService;


    public Optional<Utilisateur> seConnecter(String pseudo, String motDePasse) {
        return utilisateurService.authentifier(pseudo, motDePasse);
    }

    
    public ServiceResponse<Utilisateur> sInscrire(Utilisateur utilisateur){
        boolean pseudoExiste = isPseudoExistant(utilisateur.getPseudo());
        if (pseudoExiste) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH, "Ce pseudo est déjà utilisé.", utilisateur);
        }
        boolean emailExiste = isEmailExistant(utilisateur.getEmail());
        if (emailExiste) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH, "Cet e-mail est déjà utilisé.", utilisateur);
        }
        utilisateurService.ajouterUtilisateur(utilisateur);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"Utilisateur "+utilisateur.getPseudo() +" créé avec succès",utilisateur);
    }

    
    public ServiceResponse<Utilisateur> modifierProfil(Utilisateur utilisateur){
        boolean updated = utilisateurService.updateUtilisateur(utilisateur);
        if (!updated) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH,"Impossible de mettre à jour l'utilisateur (ID inexistant ?).",utilisateur);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS,"L'utilisateur " +utilisateur.getPseudo() + " a été modifié." ,utilisateur);
    }

    
    public boolean supprimerCompte(Integer id) {
        Optional<Utilisateur> optUser = utilisateurService.getUtilisateurById(id);
        if (optUser.isEmpty()) {
            return false;
        }
        
        Utilisateur user = optUser.get();
        user.setSuppr(true);
        
        return utilisateurService.updateUtilisateur(user);
    }
    
    public ServiceResponse<Optional<Utilisateur>> getUtilisateurById(Integer id) {
        Optional<Utilisateur> utilisateurRecherche = utilisateurService.getUtilisateurById(id);

        if (utilisateurRecherche.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur est introuvable", null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'utilisateur " +utilisateurRecherche.get().getPseudo() + " a été trouvé", utilisateurRecherche);
    }

    
    public ServiceResponse<Optional<Utilisateur>> getUtilisateurByPseudo(String pseudo) {
        Optional<Utilisateur> utilisateurRecherche = utilisateurService.getUtilisateurByPseudo(pseudo);

        if (utilisateurRecherche.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur est introuvable", null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'utilisateur " +utilisateurRecherche.get().getPseudo() + " a été trouvé", utilisateurRecherche);

    }

    
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs().stream()
                .filter(u -> !u.getSuppr())
                .collect(Collectors.toList());
    }

    
    public ServiceResponse<Utilisateur> ajouterCredit(Integer utilisateurId, Integer montant) throws Exception {
        Optional<Utilisateur> optUser = utilisateurService.getUtilisateurById(utilisateurId);
        if (optUser.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND,"Utilisateur inexistant (ID: " + utilisateurId + ").",null);
        }
        Utilisateur user = optUser.get();
        user.setCredit(user.getCredit() + montant);
        utilisateurService.updateUtilisateur(user);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "Le crédit a été ajouté à'utilisateur " +user.getPseudo(), user);
    }

    
    public boolean isPseudoExistant(String pseudo) {
        return utilisateurService.isPseudoExistant(pseudo);
    }

    
    public boolean isEmailExistant(String email) {
        return utilisateurService.isEmailExistant(email);
    }

    
    public ServiceResponse <Optional<Utilisateur>> getUtilisateurByEmail(String email) {
        Optional<Utilisateur> utilisateurRecherche = utilisateurService.getUtilisateurByEmail(email);

        if (utilisateurRecherche.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur est introuvable", null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'utilisateur " +utilisateurRecherche.get().getPseudo() + " a été trouvé", utilisateurRecherche);

    }

    public ServiceResponse<String> addArticleAVendre(Utilisateur utilisateur, ArticleVendu articleAVendre) {
        Optional<Utilisateur> utilisateurArticle = utilisateurService.getUtilisateurById(utilisateur.getId());
        if (utilisateurArticle.isEmpty()){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur n'a pas été trouvé", "");
        }


        if (articleAVendre==null){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "Aucun article correspondant","");
        }
        utilisateur.getArticleVenduList().add(articleAVendre);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "L'article a bien été ajouté à la vente","") ;
    }

    
    public ServiceResponse<String> removeCredits(Utilisateur utilisateur, int montant) {

        Optional<Utilisateur> utilisateurArticle = utilisateurService.getUtilisateurById(utilisateur.getId());
        if (utilisateurArticle.isEmpty()){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur n'a pas été trouvé", "");
        }

        if (montant >= utilisateur.getCredit()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH, "L'utilisateur n'a pas assez de crédit", "");
        }
        utilisateur.setCredit(utilisateur.getCredit() - montant);
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "Le crédit a bien été débité","") ;

    }

    
    public ServiceResponse<String> addCredits(Utilisateur utilisateur, int montant) {
        Optional<Utilisateur> utilisateurArticle = utilisateurService.getUtilisateurById(utilisateur.getId());
        if (utilisateurArticle.isEmpty()){
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur n'a pas été trouvé", "");
        }

        utilisateur.setCredit(utilisateur.getCredit() + montant);

        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "Le crédit a bien été ajouté","") ;
    }
}

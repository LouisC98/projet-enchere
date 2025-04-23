package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.ArticleVendu;
import fr.eni.encheres.bo.Utilisateur;
import fr.eni.encheres.service.response.ServiceConstant;
import fr.eni.encheres.service.response.ServiceResponse;
import fr.eni.encheres.service.user.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurServiceImpl {


    @Autowired
    UtilisateurService utilisateurService;




    public Optional<Utilisateur> seConnecter(String pseudo, String motDePasse) {
        return utilisateurService.authentifier(pseudo, motDePasse);
    }

    
    public ServiceResponse<Utilisateur> sInscrire(Utilisateur utilisateur){

        //A changer par un simple boolean
        if (isPseudoExistant(utilisateur.getPseudo())) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH, "Ce pseudo est déjà utilisé.", utilisateur);
        }
        if (isEmailExistant(utilisateur.getEmail())) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_TCH, "Cet e-mail est déjà utilisé.", utilisateur);
        }
        Utilisateur nouvelUtilisateur = utilisateurService.ajouterUtilisateur(utilisateur);
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
        return utilisateurService.deleteUtilisateur(id);
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

    
    public ServiceResponse<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurList= utilisateurService.getAllUtilisateurs();
        if (utilisateurList.isEmpty()) {
            return ServiceResponse.buildResponse(ServiceConstant.CD_ERR_NOT_FOUND, "L'utilisateur est introuvable", null);
        }
        return ServiceResponse.buildResponse(ServiceConstant.CD_SUCCESS, "La liste des utilisateur a été récupéré avec succès", utilisateurList);

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

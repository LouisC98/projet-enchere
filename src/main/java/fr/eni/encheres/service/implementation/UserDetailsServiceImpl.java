package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bo.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UtilisateurServiceImpl utilisateurServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Utilisateur> optUtilisateur = utilisateurServiceImpl.getUtilisateurByPseudo(login).data;

        if (!optUtilisateur.isPresent()) {
            optUtilisateur = utilisateurServiceImpl.getUtilisateurByEmail(login).data;
        }

        if (!optUtilisateur.isPresent()) {
            throw new UsernameNotFoundException("Utilisateur introuvable pour le login: " + login);
        }

        Utilisateur utilisateur = optUtilisateur.get();

        String role = utilisateur.getAdministrateur() ? "ROLE_ADMIN" : "ROLE_USER";
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        return new User(
                utilisateur.getPseudo(), 
                utilisateur.getMotDePasse(),
                authorities
        );
    }
}

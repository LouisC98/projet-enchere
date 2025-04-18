package fr.eni.encheres.service.implementation;

import fr.eni.encheres.bll.UtilisateurMock;
import fr.eni.encheres.bo.Utilisateur;
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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Utilisateur> optUtilisateur = UtilisateurMock.getUtilisateurByPseudo(login);

        if (optUtilisateur.isEmpty()) {
            optUtilisateur = UtilisateurMock.getUtilisateurByEmail(login);
        }

        if (optUtilisateur.isEmpty()) {
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

package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 Fait le lien entre les utilisateurs de la base et Spring Security
 **/
@Service
public class AuthentificationService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;

    public AuthentificationService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login){
       Integer id = convertirEnIdentifiant(login);
       Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("Le login " + login + " est inconnu"));
        return User.withUsername(login).password(utilisateur.getMotDePasse()).roles(utilisateur.getProfil()).build();
    }

    /**
      Convertit le login saisi en identifiant numérique.
     */
    private Integer convertirEnIdentifiant(String login) {
        try {
            return Integer.parseInt(login);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Le login " + login + " n'est pas un identifiant valide", e);
        }
    }
}


package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.service.UtilisateurService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class UtilisateurConnecteAdvice {

    private final UtilisateurService utilisateurService;

    public UtilisateurConnecteAdvice(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @ModelAttribute("utilisateurConnecte")
    public Utilisateur utilisateurConnecte(Principal principal) {
        if (principal == null) {
            return null;
        }
        return utilisateurService.trouverParIdentifiant(Integer.parseInt(principal.getName()));


    }
}

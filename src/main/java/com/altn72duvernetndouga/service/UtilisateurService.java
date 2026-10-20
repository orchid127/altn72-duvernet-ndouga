package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.UtilisateurIntrouvableException;
import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

//Cas d’usage liés aux utilisateurs de l’application
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    //    Retourne un utilistauer en fonction de son id, lance une exception si l'utilisateur n'est pas trouvé
    public Utilisateur trouverParIdentifiant(Integer id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new UtilisateurIntrouvableException(id));
    }
}

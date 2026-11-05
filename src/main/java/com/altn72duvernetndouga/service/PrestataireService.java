package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.dto.ChoixPrestataire;
import com.altn72duvernetndouga.exception.PrestataireIntrouvableException;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.repository.PrestataireRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Cas d'usage liés aux prestataires.
 */
@Service
public class PrestataireService {

    private static final String MENTION_SURVEILLANCE = " (sous surveillance)";

    private final PrestataireRepository prestataireRepository;
    private final SurveillanceService surveillanceService;

    public PrestataireService(PrestataireRepository prestataireRepository, SurveillanceService surveillanceService) {
        this.prestataireRepository = prestataireRepository;
        this.surveillanceService = surveillanceService;
    }

    /**
     * Retrouve un prestataire à partir de son identifiant, ou lève une exception s'il n'existe pas.
     */
    public Prestataire trouverParIdentifiant(Integer id) {
        return prestataireRepository.findById(id)
                .orElseThrow(() -> new PrestataireIntrouvableException(id));
    }

    /**
     * Liste de choix du formulaire d'affectation : le libellé d'un prestataire sous surveillance
     * contient la mention « (sous surveillance) », rien n'est ajouté pour les autres.
     */
    public List<ChoixPrestataire> listerChoixAffectation() {
        List<ChoixPrestataire> choix = new ArrayList<>();
        for (Prestataire prestataire : prestataireRepository.findAll()) {
            String libelle = prestataire.getNom();
            if (surveillanceService.estSousSurveillance(prestataire)) {
                libelle = libelle + MENTION_SURVEILLANCE;
            }
            choix.add(new ChoixPrestataire(prestataire.getId(), libelle));
        }
        return choix;
    }
}
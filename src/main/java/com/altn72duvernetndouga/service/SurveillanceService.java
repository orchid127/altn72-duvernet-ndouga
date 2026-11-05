package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.repository.EvaluationRepository;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Détermine si un prestataire est sous surveillance : la moyenne de ses 5 dernières évaluations
 * est strictement inférieure à 3. Le statut est toujours recalculé, jamais enregistré en base (PS4).
 */
@Service
public class SurveillanceService {

    private static final int TAILLE_FENETRE = 5;
    private static final double SEUIL_SURVEILLANCE = 3.0;

    private final EvaluationRepository evaluationRepository;

    public SurveillanceService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    /**
     * Moyenne des 5 dernières évaluations du prestataire (PS1).
     * Renvoie null s'il a moins de 5 évaluations, ou s'il n'y a pas de prestataire (PS2).
     */
    public Double moyenneDernieresEvaluations(Prestataire prestataire) {
        if (prestataire == null) {
            return null;
        }
        List<Evaluation> dernieresEvaluations = evaluationRepository
                .findByIdIncident_IdPrestataireOrderByDateEvaluationDescIdDesc(prestataire, Limit.of(TAILLE_FENETRE));
        if (dernieresEvaluations.size() < TAILLE_FENETRE) {
            return null;
        }
        double total = 0;
        for (Evaluation evaluation : dernieresEvaluations) {
            total += evaluation.getNote();
        }
        return total / TAILLE_FENETRE;
    }

    /**
     * Vrai si la moyenne des 5 dernières évaluations existe et est strictement inférieure à 3,0 (PS3).
     * Une moyenne de 3,0 exactement ne suffit pas.
     */
    public boolean estSousSurveillance(Prestataire prestataire) {
        Double moyenne = moyenneDernieresEvaluations(prestataire);
        return moyenne != null && moyenne < SEUIL_SURVEILLANCE;
    }
}
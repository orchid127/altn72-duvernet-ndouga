package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.EvaluationImpossibleException;
import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.repository.EvaluationRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Cas d'usage liés aux évaluations des prestataires.
 */
@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final IncidentService incidentService;
    private final Clock horloge;

    public EvaluationService(EvaluationRepository evaluationRepository, IncidentService incidentService, Clock horloge) {
        this.evaluationRepository = evaluationRepository;
        this.incidentService = incidentService;
        this.horloge = horloge;
    }

    /**
     * Renvoie l'évaluation d'un incident, ou une boîte vide s'il n'a pas encore été évalué.
     */
    public Optional<Evaluation> trouverPourIncident(Incident incident) {
        return evaluationRepository.findByIdIncident(incident);
    }

    /**
     * Enregistre l'évaluation d'un incident en respectant RG8 : l'incident doit être résolu
     * et affecté à un prestataire, il ne peut être évalué qu'une fois, et la note va de 1 à 5.
     */
    public Evaluation evaluer(Integer incidentId, Integer note, String commentaire) {
        Incident incident = incidentService.trouverParIdentifiant(incidentId);

        if (!incident.estResolu() || incident.getIdPrestataire() == null) {
            throw new EvaluationImpossibleException(
                    "L'incident " + incidentId + " doit être résolu et affecté à un prestataire pour être évalué");
        }
        if (evaluationRepository.findByIdIncident(incident).isPresent()) {
            throw new EvaluationImpossibleException("L'incident " + incidentId + " a déjà été évalué");
        }
        if (note < 1 || note > 5) {
            throw new EvaluationImpossibleException("La note doit être comprise entre 1 et 5");
        }

        Evaluation evaluation = new Evaluation(incident, note, commentaire, LocalDate.now(horloge));
        return evaluationRepository.save(evaluation);
    }

    /**
     * Renvoie la note moyenne d'un prestataire, ou null s'il n'a pas encore été évalué.
     */
    public Double noteMoyenne(Prestataire prestataire) {
        return evaluationRepository.calculerNoteMoyenne(prestataire);
    }
}
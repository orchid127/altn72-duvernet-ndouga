package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.repository.EvaluationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public Optional<Evaluation> trouverPourIncident(Incident incident){
        return evaluationRepository.findByIdIncident(incident);
    }
}

package com.altn72duvernetndouga.repository;

import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation,Integer> {
     Optional<Evaluation> findByIdIncident(Incident incident);
}

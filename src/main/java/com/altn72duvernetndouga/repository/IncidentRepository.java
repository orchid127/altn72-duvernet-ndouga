package com.altn72duvernetndouga.repository;

import com.altn72duvernetndouga.modele.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Accès aux incidents en base.
 */
public interface IncidentRepository extends JpaRepository<Incident, Integer> {

    /**
     * Incidents résolus, du plus récemment résolu au plus ancien (RG7).
     * Sert à l'historique (F9) et à son export en CSV.
     */
    List<Incident> findByDateResolutionIsNotNullOrderByDateResolutionDesc();
}
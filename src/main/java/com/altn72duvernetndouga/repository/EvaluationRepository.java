package com.altn72duvernetndouga.repository;

import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Accès aux évaluations en base.
 */
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {

     Optional<Evaluation> findByIdIncident(Incident incident);

     /**
      * Moyenne des notes reçues par un prestataire, ou null s'il n'a encore aucune évaluation.
      */
     @Query("SELECT AVG(e.note) FROM Evaluation e WHERE e.idIncident.idPrestataire = :prestataire")
     Double calculerNoteMoyenne(@Param("prestataire") Prestataire prestataire);

     /**
      * Évaluations des incidents affectés à un prestataire, de la plus récente à la plus ancienne
      * (date d'évaluation décroissante, puis identifiant décroissant : PS1), limitées au nombre demandé.
      */
     List<Evaluation> findByIdIncident_IdPrestataireOrderByDateEvaluationDescIdDesc(Prestataire prestataire, Limit limite);
}

package com.altn72duvernetndouga.dto;

import com.altn72duvernetndouga.modele.Incident;

import java.time.Instant;

/**
 * Informations d'un incident renvoyées par l'API REST (F12).
 * On n'expose pas directement l'entité, pour que le contrat de l'API ne dépende pas de la base.
 */
public record IncidentDto(Integer id,
                          String description,
                          String statut,
                          String referenceEquipement,
                          Instant dateOuverture) {

    /**
     * Construit le DTO à partir d'un incident.
     */
    public static IncidentDto depuis(Incident incident) {
        return new IncidentDto(
                incident.getId(),
                incident.getDescription(),
                incident.getStatut(),
                incident.getIdEquipement().getReference(),
                incident.getDateOuverture());
    }
}
package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.dto.IncidentDto;
import com.altn72duvernetndouga.service.IncidentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST des incidents (F12) : consultation, et déclaration de la résolution d'un incident.
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentApiControleur {

    private final IncidentService incidentService;

    public IncidentApiControleur(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    /**
     * Renvoie tous les incidents, triés par date d'ouverture.
     */
    @GetMapping
    public List<IncidentDto> listerIncidents() {
        return incidentService.listerIncidentsParDate().stream()
                .map(IncidentDto::depuis)
                .toList();
    }

    /**
     * Renvoie un incident. Si l'incident n'existe pas, le gestionnaire d'erreurs répond 404.
     */
    @GetMapping("/{id}")
    public IncidentDto afficherIncident(@PathVariable Integer id) {
        return IncidentDto.depuis(incidentService.trouverParIdentifiant(id));
    }

    /**
     * Déclare un incident résolu : enregistre la date et l'heure de résolution (F8).
     */
    @PatchMapping("/{id}/resolution")
    public IncidentDto resoudreIncident(@PathVariable Integer id) {
        return IncidentDto.depuis(incidentService.enregistrerDateResolutionIncident(id));
    }
}
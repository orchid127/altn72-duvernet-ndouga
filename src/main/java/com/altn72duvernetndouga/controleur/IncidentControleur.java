package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IncidentControleur {
    private final IncidentService incidentService;

    public  IncidentControleur(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/incidents")
    public ResponseEntity<List<Incident>> listerIncidentsParDate() {
        List<Incident> response = incidentService.listerIncidentsParDate();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/incidents/{idIncident}/prestataire")
    public ResponseEntity<Incident> affecterPrestataireIncident(@PathVariable int idIncident, @RequestBody Prestataire prestataire) {
        Incident response = incidentService.affecterPrestataireIncident(idIncident, prestataire);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/incidents/{idIncident}/date-resolution")
    public ResponseEntity<Incident> enregistrerDateResolution(@PathVariable int idIncident) {
        Incident response = incidentService.enregistrerDateResolutionIncident(idIncident);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}

package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.service.IncidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IncidentControleur {
    @Autowired
    private IncidentService incidentService;

    @GetMapping("/incidents")
    public List<Incident> listerIncidentsParDate() {
        return incidentService.listerIncidentsParDate();
    }

    @PutMapping("/incidents/{idIncident}/prestataire")
    public Incident affecterPrestataireIncident(@PathVariable int idIncident, @RequestBody Prestataire prestataire) {
        return incidentService.affecterPrestataireIncident(idIncident, prestataire);
    }

    @PutMapping("/incidents/{idIncident}/date-resolution")
    public Incident enregistrerDateResolution(@PathVariable int idIncident) {
        return incidentService.enregistrerDateResolutionIncident(idIncident);
    }
}

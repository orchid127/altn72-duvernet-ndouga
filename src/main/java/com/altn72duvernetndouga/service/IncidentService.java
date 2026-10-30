package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IncidentService {
    @Autowired
    private IncidentRepository incidentRepository;

    public Incident affecterPrestataireIncident(int idIncident, Prestataire prestataire) {
        if (incidentRepository.findById(idIncident).isPresent()) {
            Incident incident = incidentRepository.findById(idIncident).get();
            if (!incident.getStatut().equals("Résolu")) {
                incident.setIdPrestataire(prestataire);
                incident.setStatut("Affecté à un prestataire");
            }
            return incidentRepository.save(incident);
        }
        return null;
    }

    public Incident enregistrerDateResolutionIncident(int idIncident) {
        if (incidentRepository.findById(idIncident).isPresent()) {
            Incident incident = incidentRepository.findById(idIncident).get();
            incident.setDateResolution(Instant.now());
            incident.setStatut("Résolu");
            return incidentRepository.save(incident);
        }
        return null;
    }

    public List<Incident> listerIncidentsParDate() {
        return incidentRepository.findAll().stream().sorted((i1, i2) -> i1.getDateOuverture().compareTo(i2.getDateOuverture())).toList();
    }
}

package com.altn72duvernetndouga.service;


import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
public class IncidentService {

    private static final String STATUT_NON_AFFECTE = "Non affecté";
    private final IncidentRepository incidentRepository;
    private final EquipementService equipementService;
    private final Clock clock;

    public IncidentService(IncidentRepository incidentRepository, EquipementService equipementService, Clock clock) {
        this.incidentRepository = incidentRepository;
        this.equipementService = equipementService;
        this.clock = clock;
    }

    public Incident declarer(Integer equipementId, String description){
        Equipement equipement = equipementService.trouverParIdentifiant(equipementId);
        Incident incident = new Incident(equipement, description, Instant.now(clock),STATUT_NON_AFFECTE);
        return incidentRepository.save(incident);
    }
}

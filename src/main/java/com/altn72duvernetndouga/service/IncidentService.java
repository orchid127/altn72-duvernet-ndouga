package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.IncidentResoluException;
import com.altn72duvernetndouga.exception.IncidentIntrouvableException;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

/**
 * Cas d'usage liés aux incidents : déclaration, consultation, modification, affectation et résolution.
 */
@Service
public class IncidentService {

    private static final String STATUT_NON_AFFECTE = "Non affecté";
    private static final String STATUT_AFFECTE = "Affecté à un prestataire";
    private static final String STATUT_RESOLU = "Résolu";

    private final IncidentRepository incidentRepository;
    private final EquipementService equipementService;
    private final Clock horloge;

    public IncidentService(IncidentRepository incidentRepository, EquipementService equipementService, Clock horloge) {
        this.incidentRepository = incidentRepository;
        this.equipementService = equipementService;
        this.horloge = horloge;
    }

    /**
     * Déclare un incident sur un équipement : il démarre sans prestataire,
     * avec le statut « Non affecté » (RG1).
     */
    public Incident declarer(Integer equipementId, String description) {
        Equipement equipement = equipementService.trouverParIdentifiant(equipementId);
        Incident incident = new Incident(equipement, description, Instant.now(horloge), STATUT_NON_AFFECTE);
        return incidentRepository.save(incident);
    }

    /**
     * Retrouve un incident à partir de son identifiant, ou lève une exception s'il n'existe pas.
     */
    public Incident trouverParIdentifiant(Integer id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new IncidentIntrouvableException(id));
    }

    /**
     * Modifie la description d'un incident. Seule la description est modifiable,
     * et seulement tant que l'incident n'est pas résolu.
     */
    public Incident modifierDescription(Integer id, String nouvelleDescription) {
        Incident incident = trouverParIdentifiant(id);
        if (incident.estResolu()) {
            throw new IncidentResoluException(id);
        }
        incident.setDescription(nouvelleDescription);
        return incidentRepository.save(incident);
    }

    /**
     * Affecte ou réaffecte un prestataire à un incident non résolu (F7).
     */
    public Incident affecterPrestataireIncident(int idIncident, Prestataire prestataire) {
        Incident incident = trouverParIdentifiant(idIncident);
        if (!incident.getStatut().equals(STATUT_RESOLU)) {
            incident.setIdPrestataire(prestataire);
            incident.setStatut(STATUT_AFFECTE);
        }
        return incidentRepository.save(incident);
    }

    /**
     * Déclare un incident résolu en enregistrant la date et l'heure de résolution (F8).
     */
    public Incident enregistrerDateResolutionIncident(int idIncident) {
        Incident incident = trouverParIdentifiant(idIncident);
        incident.setDateResolution(Instant.now(horloge));
        incident.setStatut(STATUT_RESOLU);
        return incidentRepository.save(incident);
    }

    /**
     * Renvoie tous les incidents, triés par date d'ouverture.
     */
    public List<Incident> listerIncidentsParDate() {
        return incidentRepository.findAll().stream()
                .sorted((i1, i2) -> i1.getDateOuverture().compareTo(i2.getDateOuverture()))
                .toList();
    }

    public List<Incident> listerIncidentsNonResolus() {
        return incidentRepository.findByStatutIsResolu();
    }
}
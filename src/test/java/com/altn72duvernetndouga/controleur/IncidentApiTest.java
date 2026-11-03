package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.EquipementRepository;
import com.altn72duvernetndouga.repository.EvaluationRepository;
import com.altn72duvernetndouga.repository.IncidentRepository;
import com.altn72duvernetndouga.repository.PrestataireRepository;
import com.altn72duvernetndouga.repository.StationRepository;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie le comportement de l'API REST (F12) : consultation, codes HTTP adaptés,
 * changement d'état et restriction de la résolution au responsable (RG11).
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "1", roles = "RESPONSABLE")
class IncidentApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    private Incident incident;

    /**
     * Repart d'une base propre avant chaque test : l'utilisateur 1, une station,
     * un équipement et un incident non résolu.
     */
    @BeforeEach
    void preparerDonnees() {
        evaluationRepository.deleteAll();
        incidentRepository.deleteAll();
        equipementRepository.deleteAll();
        stationRepository.deleteAll();
        prestataireRepository.deleteAll();
        utilisateurRepository.deleteAll();

        utilisateurRepository.save(new Utilisateur(1, "inutile-ici", "Camille", "RESPONSABLE"));

        Station station = new Station();
        station.setNomStation("Gare du Nord");
        station = stationRepository.save(station);

        Equipement equipement = new Equipement();
        equipement.setReference("ASC-TEST-01");
        equipement.setDescription("Ascenseur de test");
        equipement.setType("Ascenseur");
        equipement.setEtatFontionnement("En marche");
        equipement.setIdStation(station);
        equipement = equipementRepository.save(equipement);

        incident = incidentRepository.save(
                new Incident(equipement, "Ascenseur bloqué au deuxième étage", Instant.now(), "Non affecté"));
    }

    @Test
    void listeDesIncidentsContientLIncident() throws Exception {
        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(incident.getId()));
    }

    @Test
    void incidentExistantEstRenvoye() throws Exception {
        mockMvc.perform(get("/api/incidents/" + incident.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Ascenseur bloqué au deuxième étage"))
                .andExpect(jsonPath("$.statut").value("Non affecté"))
                .andExpect(jsonPath("$.referenceEquipement").value("ASC-TEST-01"));
    }

    @Test
    void incidentInexistantRenvoie404AvecUnMessage() throws Exception {
        mockMvc.perform(get("/api/incidents/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void resolutionPasseLeStatutAResolu() throws Exception {
        mockMvc.perform(patch("/api/incidents/" + incident.getId() + "/resolution"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("Résolu"));

        Incident incidentResolu = incidentRepository.findById(incident.getId()).get();
        assertThat(incidentResolu.getDateResolution()).isNotNull();
    }

    @Test
    @WithMockUser(username = "2", roles = "AGENT")
    void agentNePeutPasResoudreUnIncident() throws Exception {
        mockMvc.perform(patch("/api/incidents/" + incident.getId() + "/resolution"))
                .andExpect(status().isForbidden());
    }
}

package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.EquipementRepository;
import com.altn72duvernetndouga.repository.IncidentRepository;
import com.altn72duvernetndouga.repository.StationRepository;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Vérifie le comportement de F4 : déclaration d'un incident valide (RG1) et refus d'un formulaire invalide.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "1")
class DeclarationIncidentTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    private Equipement equipement;

    /**
     * Repart d'une base propre avant chaque test : l'utilisateur 1 (pour l'en-tête),
     * une station et un équipement sur lequel déclarer les incidents.
     */
    @BeforeEach
    void preparerDonnees() {
        incidentRepository.deleteAll();
        equipementRepository.deleteAll();
        stationRepository.deleteAll();
        utilisateurRepository.deleteAll();

        utilisateurRepository.save(new Utilisateur(1, "inutile-ici", "Camille", "RESPONSABLE"));

        Station station = new Station();
        station.setNomStation("Gare du Nord");
        station = stationRepository.save(station);

        Equipement nouvelEquipement = new Equipement();
        nouvelEquipement.setReference("ASC-TEST-01");
        nouvelEquipement.setDescription("Ascenseur de test");
        nouvelEquipement.setType("Ascenseur");
        nouvelEquipement.setEtatFontionnement("En marche");
        nouvelEquipement.setIdStation(station);
        equipement = equipementRepository.save(nouvelEquipement);
    }

    @Test
    void declarationValideCreeUnIncidentNonAffecte() throws Exception {
        mockMvc.perform(post("/incidents")
                        .param("equipementId", String.valueOf(equipement.getId()))
                        .param("description", "Ascenseur bloqué au deuxième étage")
                        .with(csrf()))
                .andExpect(redirectedUrl("/"));

        List<Incident> incidents = incidentRepository.findAll();
        assertThat(incidents).hasSize(1);
        Incident incident = incidents.get(0);
        assertThat(incident.getStatut()).isEqualTo("Non affecté");
        assertThat(incident.getIdPrestataire()).isNull();
        assertThat(incident.getDateOuverture()).isNotNull();
    }

    @Test
    void formulaireVideEstRefuseAvecMessages() throws Exception {
        mockMvc.perform(post("/incidents")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("declarer-incident"))
                .andExpect(model().attributeHasFieldErrors("formulaire", "equipementId", "description"));

        assertThat(incidentRepository.count()).isZero();
    }

    @Test
    void descriptionTropCourteEstRefusee() throws Exception {
        mockMvc.perform(post("/incidents")
                        .param("equipementId", String.valueOf(equipement.getId()))
                        .param("description", "abc")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("declarer-incident"))
                .andExpect(model().attributeHasFieldErrors("formulaire", "description"));

        assertThat(incidentRepository.count()).isZero();
    }
}
package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.exception.IncidentResoluException;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.EquipementRepository;
import com.altn72duvernetndouga.repository.IncidentRepository;
import com.altn72duvernetndouga.repository.StationRepository;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import com.altn72duvernetndouga.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

/**
 * Vérifie le comportement de F6 : modification de la description, refusée pour un incident résolu.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "1")
class ModificationIncidentTest {

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

    @Autowired
    private IncidentService incidentService;

    private Incident incident;

    /**
     * Repart d'une base propre avant chaque test : l'utilisateur 1 (pour l'en-tête),
     * une station, un équipement et un incident non résolu à modifier.
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

        Equipement equipement = new Equipement();
        equipement.setReference("ASC-TEST-01");
        equipement.setDescription("Ascenseur de test");
        equipement.setType("Ascenseur");
        equipement.setEtatFontionnement("En marche");
        equipement.setIdStation(station);
        equipement = equipementRepository.save(equipement);

        incident = incidentRepository.save(
                new Incident(equipement, "Description de départ", Instant.now(), "Non affecté"));
    }

    @Test
    void modificationValideChangeLaDescription() throws Exception {
        mockMvc.perform(post("/incidents/" + incident.getId() + "/modifier")
                        .param("description", "Ascenseur bloqué au deuxième étage")
                        .with(csrf()))
                .andExpect(redirectedUrl("/incidents/" + incident.getId()));

        Incident incidentModifie = incidentRepository.findById(incident.getId()).get();
        assertThat(incidentModifie.getDescription()).isEqualTo("Ascenseur bloqué au deuxième étage");
    }

    @Test
    void incidentResoluNePeutPasEtreModifie() {
        incident.setDateResolution(Instant.now());
        incidentRepository.save(incident);

        assertThatThrownBy(() -> incidentService.modifierDescription(incident.getId(), "Une nouvelle description"))
                .isInstanceOf(IncidentResoluException.class);
    }
}
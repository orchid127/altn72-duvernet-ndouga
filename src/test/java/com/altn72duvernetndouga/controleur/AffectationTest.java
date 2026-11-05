package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
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
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Vérifie l'interface de l'évolution « Prestataire sous surveillance » :
 * la mention dans la liste de choix, la confirmation exigée (PS5) et la restriction au responsable.
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "1", roles = "RESPONSABLE")
class AffectationTest {

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

    private Equipement equipement;
    private Prestataire prestataireSurveille;
    private Prestataire prestataireFiable;
    private Incident incidentAAffecter;

    /**
     * Repart d'une base propre avant chaque test : l'utilisateur 1, un prestataire sous surveillance
     * (5 évaluations notées 2), un prestataire sans évaluation et un incident non affecté.
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

        Equipement nouvelEquipement = new Equipement();
        nouvelEquipement.setReference("ASC-TEST-01");
        nouvelEquipement.setDescription("Ascenseur de test");
        nouvelEquipement.setType("Ascenseur");
        nouvelEquipement.setEtatFontionnement("En marche");
        nouvelEquipement.setIdStation(station);
        equipement = equipementRepository.save(nouvelEquipement);

        prestataireSurveille = creerPrestataire("Dupont Services");
        prestataireFiable = creerPrestataire("Martin Ascenseurs");
        creerEvaluations(prestataireSurveille, 2, 2, 2, 2, 2);

        incidentAAffecter = incidentRepository.save(
                new Incident(equipement, "Ascenseur bloqué", Instant.now(), "Non affecté"));
    }

    /**
     * Crée et enregistre un prestataire.
     */
    private Prestataire creerPrestataire(String nom) {
        Prestataire prestataire = new Prestataire();
        prestataire.setNom(nom);
        prestataire.setMail("contact@exemple.fr");
        return prestataireRepository.save(prestataire);
    }

    /**
     * Crée une évaluation par note, chacune sur un incident résolu et affecté au prestataire.
     */
    private void creerEvaluations(Prestataire prestataire, int... notes) {
        for (int position = 0; position < notes.length; position++) {
            Incident incident = new Incident(equipement, "Panne de test", Instant.now(), "Résolu");
            incident.setIdPrestataire(prestataire);
            incident.setDateResolution(Instant.now());
            incident = incidentRepository.save(incident);
            evaluationRepository.save(new Evaluation(incident, notes[position], null,
                    LocalDate.of(2026, 10, 1).plusDays(position)));
        }
    }

    @Test
    void laListeDeChoixMentionneLePrestataireSousSurveillance() throws Exception {
        mockMvc.perform(get("/incidents/" + incidentAAffecter.getId() + "/affectation"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dupont Services (sous surveillance)")))
                .andExpect(content().string(containsString("Martin Ascenseurs<")));
    }

    @Test
    void sansConfirmationLAffectationAUnPrestataireSurveilleEstRefuseeEtRienNestEnregistre() throws Exception {
        mockMvc.perform(post("/incidents/" + incidentAAffecter.getId() + "/affectation")
                        .param("prestataireId", String.valueOf(prestataireSurveille.getId()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("affecter-incident"))
                .andExpect(content().string(containsString("surveillance")));

        Incident incident = incidentRepository.findById(incidentAAffecter.getId()).get();
        assertThat(incident.getIdPrestataire()).isNull();
        assertThat(incident.getStatut()).isEqualTo("Non affecté");
    }

    @Test
    void avecConfirmationLAffectationAUnPrestataireSurveilleEstEnregistree() throws Exception {
        mockMvc.perform(post("/incidents/" + incidentAAffecter.getId() + "/affectation")
                        .param("prestataireId", String.valueOf(prestataireSurveille.getId()))
                        .param("confirmation", "true")
                        .with(csrf()))
                .andExpect(redirectedUrl("/incidents/" + incidentAAffecter.getId()));

        Incident incident = incidentRepository.findById(incidentAAffecter.getId()).get();
        assertThat(incident.getIdPrestataire().getId()).isEqualTo(prestataireSurveille.getId());
        assertThat(incident.getStatut()).isEqualTo("Affecté à un prestataire");
    }

    @Test
    void unPrestataireNonSurveilleSAffecteSansConfirmation() throws Exception {
        mockMvc.perform(post("/incidents/" + incidentAAffecter.getId() + "/affectation")
                        .param("prestataireId", String.valueOf(prestataireFiable.getId()))
                        .with(csrf()))
                .andExpect(redirectedUrl("/incidents/" + incidentAAffecter.getId()));

        Incident incident = incidentRepository.findById(incidentAAffecter.getId()).get();
        assertThat(incident.getIdPrestataire().getId()).isEqualTo(prestataireFiable.getId());
    }

    @Test
    @WithMockUser(username = "2", roles = "AGENT")
    void unAgentNePeutPasAffecter() throws Exception {
        mockMvc.perform(get("/incidents/" + incidentAAffecter.getId() + "/affectation"))
                .andExpect(status().isForbidden());
    }
}
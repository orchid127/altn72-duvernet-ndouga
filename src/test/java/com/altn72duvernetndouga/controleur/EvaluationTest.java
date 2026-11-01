package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.exception.EvaluationImpossibleException;
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
import com.altn72duvernetndouga.service.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie le comportement de F10 : les règles de RG8 et la restriction au responsable (RG11).
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "1", roles = "RESPONSABLE")
class EvaluationTest {

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

    @Autowired
    private EvaluationService evaluationService;

    private Equipement equipement;
    private Prestataire prestataire;
    private Incident incidentResolu;

    /**
     * Repart d'une base propre avant chaque test : l'utilisateur 1, une station, un équipement,
     * un prestataire et un incident résolu et affecté, donc prêt à être évalué.
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

        Prestataire nouveauPrestataire = new Prestataire();
        nouveauPrestataire.setNom("Ascenseurs Express");
        nouveauPrestataire.setMail("contact@ascenseurs-express.fr");
        prestataire = prestataireRepository.save(nouveauPrestataire);

        incidentResolu = creerIncidentResoluEtAffecte();
    }

    /**
     * Crée et enregistre un incident résolu et affecté au prestataire de test.
     */
    private Incident creerIncidentResoluEtAffecte() {
        Incident incident = new Incident(equipement, "Ascenseur bloqué", Instant.now(), "Non affecté");
        incident.setIdPrestataire(prestataire);
        incident.setDateResolution(Instant.now());
        incident.setStatut("Résolu");
        return incidentRepository.save(incident);
    }

    @Test
    void evaluationValideEstEnregistree() throws Exception {
        mockMvc.perform(post("/incidents/" + incidentResolu.getId() + "/evaluation")
                        .param("note", "4")
                        .param("commentaire", "Intervention rapide")
                        .with(csrf()))
                .andExpect(redirectedUrl("/incidents/" + incidentResolu.getId()));

        List<Evaluation> evaluations = evaluationRepository.findAll();
        assertThat(evaluations).hasSize(1);
        assertThat(evaluations.get(0).getNote()).isEqualTo(4);
    }

    @Test
    void incidentNonResoluNePeutPasEtreEvalue() {
        Incident incidentOuvert = incidentRepository.save(
                new Incident(equipement, "Escalator arrêté", Instant.now(), "Non affecté"));

        assertThatThrownBy(() -> evaluationService.evaluer(incidentOuvert.getId(), 4, null))
                .isInstanceOf(EvaluationImpossibleException.class);
    }

    @Test
    void secondeEvaluationEstRefusee() {
        evaluationService.evaluer(incidentResolu.getId(), 4, null);

        assertThatThrownBy(() -> evaluationService.evaluer(incidentResolu.getId(), 5, null))
                .isInstanceOf(EvaluationImpossibleException.class);
    }

    @Test
    void noteHorsLimitesEstRefusee() {
        assertThatThrownBy(() -> evaluationService.evaluer(incidentResolu.getId(), 6, null))
                .isInstanceOf(EvaluationImpossibleException.class);
    }

    @Test
    void noteMoyenneEstCalculee() {
        Incident autreIncidentResolu = creerIncidentResoluEtAffecte();
        evaluationService.evaluer(incidentResolu.getId(), 4, null);
        evaluationService.evaluer(autreIncidentResolu.getId(), 2, null);

        assertThat(evaluationService.noteMoyenne(prestataire)).isEqualTo(3.0);
    }

    @Test
    @WithMockUser(username = "2", roles = "AGENT")
    void agentNePeutPasEvaluer() throws Exception {
        mockMvc.perform(get("/incidents/" + incidentResolu.getId() + "/evaluation"))
                .andExpect(status().isForbidden());
    }
}
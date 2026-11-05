package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Evaluation;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.repository.EquipementRepository;
import com.altn72duvernetndouga.repository.EvaluationRepository;
import com.altn72duvernetndouga.repository.IncidentRepository;
import com.altn72duvernetndouga.repository.PrestataireRepository;
import com.altn72duvernetndouga.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Vérifie les règles PS1 à PS4 de l'évolution « Prestataire sous surveillance »
 * avec les 7 exemples de référence du sujet (notes de la plus ancienne à la plus récente).
 */
@SpringBootTest
class SurveillanceServiceTest {

    private static final LocalDate PREMIER_JOUR = LocalDate.of(2026, 10, 1);
    private static final double PRECISION = 0.001;

    @Autowired
    private SurveillanceService surveillanceService;

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
    private Prestataire prestataire;

    /**
     * Repart d'une base propre avant chaque test : une station, un équipement et un prestataire sans évaluation.
     */
    @BeforeEach
    void preparerDonnees() {
        evaluationRepository.deleteAll();
        incidentRepository.deleteAll();
        equipementRepository.deleteAll();
        stationRepository.deleteAll();
        prestataireRepository.deleteAll();

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
        nouveauPrestataire.setNom("Dupont Réseaux");
        nouveauPrestataire.setMail("contact@dupont-reseaux.fr");
        prestataire = prestataireRepository.save(nouveauPrestataire);
    }

    /**
     * Crée une évaluation par note, de la plus ancienne à la plus récente : chaque note porte
     * sur un incident résolu et affecté au prestataire, et est datée d'un jour de plus que la précédente.
     */
    private void creerEvaluations(int... notes) {
        for (int position = 0; position < notes.length; position++) {
            Incident incident = new Incident(equipement, "Panne de test", Instant.now(), "Résolu");
            incident.setIdPrestataire(prestataire);
            incident.setDateResolution(Instant.now());
            incident = incidentRepository.save(incident);
            evaluationRepository.save(
                    new Evaluation(incident, notes[position], null, PREMIER_JOUR.plusDays(position)));
        }
    }

    @Test
    void exemple1MoinsDeCinqEvaluationsNonSurveilleEtMoyenneNulle() {
        creerEvaluations(4, 1, 1, 1);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isNull();
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void exemple2MoyenneDeTroisExactementNonSurveille() {
        creerEvaluations(3, 3, 3, 3, 3);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(3.0, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void exemple3CinqDernieresSousTroisSurveille() {
        creerEvaluations(3, 3, 3, 3, 3, 2);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(2.8, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isTrue();
    }

    @Test
    void exemple4LaFenetreGlisseEtLePrestataireSortDeLaSurveillance() {
        creerEvaluations(3, 3, 3, 3, 3, 2, 5);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(3.2, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void exemple5UneMauvaiseNoteAncienneNaPlusDInfluence() {
        creerEvaluations(1, 5, 5, 5, 5, 5);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(5.0, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void exemple6MoyenneDeDeuxVirguleHuitSurveille() {
        creerEvaluations(2, 3, 3, 3, 3);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(2.8, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isTrue();
    }

    @Test
    void exemple7MoyenneDeTroisVirguleQuatreNonSurveille() {
        creerEvaluations(1, 1, 5, 5, 5);

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(3.4, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void aDateEgaleLIdentifiantLePlusRecentDepartage() {
        for (int note : new int[]{1, 5, 5, 5, 5, 5}) {
            Incident incident = new Incident(equipement, "Panne de test", Instant.now(), "Résolu");
            incident.setIdPrestataire(prestataire);
            incident.setDateResolution(Instant.now());
            incident = incidentRepository.save(incident);
            evaluationRepository.save(new Evaluation(incident, note, null, PREMIER_JOUR));
        }

        assertThat(surveillanceService.moyenneDernieresEvaluations(prestataire)).isCloseTo(5.0, within(PRECISION));
        assertThat(surveillanceService.estSousSurveillance(prestataire)).isFalse();
    }

    @Test
    void prestataireAbsentNestJamaisSousSurveillance() {
        assertThat(surveillanceService.moyenneDernieresEvaluations(null)).isNull();
        assertThat(surveillanceService.estSousSurveillance(null)).isFalse();
    }
}
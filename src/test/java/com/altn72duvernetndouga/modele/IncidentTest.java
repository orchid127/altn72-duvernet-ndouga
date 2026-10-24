package com.altn72duvernetndouga.modele;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Vérifie le calcul de la durée d'ouverture d'un incident, avec une horloge fixée
 * pour que le résultat ne dépende pas du moment où le test est lancé.
 */
class IncidentTest {

    private static final Instant MAINTENANT = Instant.parse("2026-10-20T12:00:00Z");
    private static final Clock HORLOGE = Clock.fixed(MAINTENANT, ZoneOffset.UTC);

    @Test
    void incidentOuvertDepuis47HeuresDure47Heures() {
        Incident incident = new Incident(null, "Ascenseur bloqué",
                MAINTENANT.minus(Duration.ofHours(47)), "Non affecté");

        assertThat(incident.dureeOuvertureEnHeures(HORLOGE)).isEqualTo(47);
    }

    @Test
    void incidentResoluCompteJusquaSaResolution() {
        Incident incident = new Incident(null, "Ascenseur bloqué",
                MAINTENANT.minus(Duration.ofHours(72)), "Non affecté");
        incident.setDateResolution(MAINTENANT.minus(Duration.ofHours(10)));

        assertThat(incident.dureeOuvertureEnHeures(HORLOGE)).isEqualTo(62);
    }
}
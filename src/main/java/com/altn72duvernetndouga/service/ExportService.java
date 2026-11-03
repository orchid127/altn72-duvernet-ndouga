package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Exporte l'historique des incidents résolus au format CSV (lot alpha, piste 9).
 */
@Service
public class ExportService {

    private static final String SEPARATEUR = ";";
    private static final String EN_TETES = "Numéro;Équipement;Station;Description;Ouverture;Résolution;Prestataire";

    private final IncidentRepository incidentRepository;

    public ExportService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    /**
     * Construit le contenu du fichier CSV : une ligne d'en-têtes, puis un incident résolu par ligne,
     * du plus récemment résolu au plus ancien (RG7).
     * Le point-virgule sert de séparateur pour qu'Excel en français ouvre le fichier correctement.
     * L'annotation Transactional en lecture seule garde la connexion à la base ouverte pendant toute la méthode :
     * l'équipement, la station et le prestataire de chaque incident sont chargés à la demande.
     */
    @Transactional(readOnly = true)
    public String exporterHistoriqueCsv() {
        List<Incident> incidents = incidentRepository.findByDateResolutionIsNotNullOrderByDateResolutionDesc();
        StringBuilder csv = new StringBuilder(EN_TETES).append("\n");
        for (Incident incident : incidents) {
            csv.append(convertirEnLigne(incident)).append("\n");
        }
        return csv.toString();
    }

    /**
     * Transforme un incident en une ligne du fichier CSV.
     */
    private String convertirEnLigne(Incident incident) {
        return incident.getId() + SEPARATEUR
                + incident.getIdEquipement().getReference() + SEPARATEUR
                + incident.getIdEquipement().getIdStation().getNomStation() + SEPARATEUR
                + entreGuillemets(incident.getDescription()) + SEPARATEUR
                + incident.getDateOuverture() + SEPARATEUR
                + incident.getDateResolution() + SEPARATEUR
                + nomDuPrestataire(incident);
    }

    /**
     * Entoure un texte de guillemets, pour qu'un point-virgule dans la description ne coupe pas la ligne.
     * Un guillemet à l'intérieur du texte est doublé, comme le prévoit le format CSV.
     */
    private String entreGuillemets(String texte) {
        return "\"" + texte.replace("\"", "\"\"") + "\"";
    }

    /**
     * Renvoie le nom du prestataire de l'incident, ou un champ vide s'il n'en a pas.
     */
    private String nomDuPrestataire(Incident incident) {
        if (incident.getIdPrestataire() == null) {
            return "";
        }
        return incident.getIdPrestataire().getNom();
    }
}

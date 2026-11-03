package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Téléchargement de l'historique des incidents résolus au format CSV (lot alpha, piste 9).
 */
@Controller
public class ExportControleur {

    private static final String MARQUE_UTF8_POUR_EXCEL = "\uFEFF";

    private final ExportService exportService;

    public ExportControleur(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * Renvoie le fichier CSV en téléchargement. ResponseEntity est utile ici (§7.4, point 5) :
     * il permet d'ajouter les en-têtes HTTP qui indiquent le type du fichier et son nom.
     * La marque UTF-8 placée au début du fichier permet à Excel d'afficher correctement les accents.
     */
    @GetMapping("/export/historique.csv")
    public ResponseEntity<String> telechargerHistorique() {
        String contenu = MARQUE_UTF8_POUR_EXCEL + exportService.exporterHistoriqueCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"historique-incidents.csv\"")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
                .body(contenu);
    }
}

package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.exception.ImportImpossibleException;
import com.altn72duvernetndouga.service.ImportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Page d'import de stations depuis un fichier CSV (lot alpha, piste 9).
 */
@Controller
public class ImportControleur {

    private final ImportService importService;

    public ImportControleur(ImportService importService) {
        this.importService = importService;
    }

    /**
     * Affiche le formulaire d'envoi du fichier.
     */
    @GetMapping("/import/stations")
    public String afficherFormulaireImport() {
        return "import-stations";
    }

    /**
     * Importe le fichier envoyé, puis réaffiche la page avec le nombre de stations créées.
     */
    @PostMapping("/import/stations")
    public String importerStations(@RequestParam("fichier") MultipartFile fichier, Model model) {
        int nombreDeStations = importService.importerStations(fichier);
        model.addAttribute("message", nombreDeStations + " station(s) importée(s).");
        return "import-stations";
    }

    /**
     * Si l'import est refusé, réaffiche la page avec un message clair, sans trace technique (§7.4).
     */
    @ExceptionHandler(ImportImpossibleException.class)
    public String importRefuse(ImportImpossibleException exception, Model model) {
        model.addAttribute("erreur", exception.getMessage());
        return "import-stations";
    }
}

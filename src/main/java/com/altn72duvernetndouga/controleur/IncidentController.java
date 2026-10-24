package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.formulaire.DeclarationIncidentForm;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.service.EquipementService;
import com.altn72duvernetndouga.service.EvaluationService;
import com.altn72duvernetndouga.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Clock;
import java.util.List;

/**
 * Pages liées aux incidents : pour l'instant, la déclaration d'un incident (F4).
 */
@Controller
public class IncidentController {

    private final IncidentService incidentService;
    private final EquipementService equipementService;
    private final EvaluationService evaluationService;
    private Clock horloge;


    public IncidentController(IncidentService incidentService, EquipementService equipementService, EvaluationService evaluationService) {
        this.incidentService = incidentService;
        this.equipementService = equipementService;
        this.evaluationService = evaluationService;
    }

    /**
     * Fournit la liste des équipements à toutes les pages de ce contrôleur,
     * pour remplir la liste déroulante du formulaire.
     */
    @ModelAttribute("equipements")
    public List<Equipement> equipements() {
        return equipementService.listerEquipements();
    }

    /**
     * Affiche le formulaire de déclaration, vide.
     */
    @GetMapping("/incidents/nouveau")
    public String afficherFormulaireDeclaration(Model model) {
        model.addAttribute("formulaire", new DeclarationIncidentForm());
        return "declarer-incident";
    }

    /**
     * Traite le formulaire envoyé : le réaffiche avec les messages d'erreur s'il est invalide,
     * sinon déclare l'incident puis redirige pour éviter un double envoi en cas de rafraîchissement.
     */
    @PostMapping("/incidents")
    public String declarerIncident(@Valid @ModelAttribute("formulaire") DeclarationIncidentForm formulaire,
                                   BindingResult resultat) {
        if (resultat.hasErrors()) {
            return "declarer-incident";
        }
        Incident incident = incidentService.declarer(formulaire.getEquipementId(), formulaire.getDescription());
        return "redirect:/incidents/" + incident.getId() ;
    }

    /**
     * Affiche le détail d'un incident : ses informations, sa durée d'ouverture
     * (calculée à partir de l'heure courante) et son évaluation éventuelle.
     */
    @GetMapping("/incidents/{id}")
    public String afficherDetail(@PathVariable Integer id, Model model) {
        Incident incident = incidentService.trouverParIdentifiant(id);
        model.addAttribute("incident", incident);
        model.addAttribute("dureeEnHeures", incident.dureeOuvertureEnHeures(horloge));
        model.addAttribute("evaluation", evaluationService.trouverPourIncident(incident).orElse(null));
        return "incident-detail";
    }

}
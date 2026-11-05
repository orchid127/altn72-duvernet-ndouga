package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.formulaire.DeclarationIncidentForm;
import com.altn72duvernetndouga.formulaire.ModificationIncidentForm;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Incident;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.service.EquipementService;
import com.altn72duvernetndouga.service.EvaluationService;
import com.altn72duvernetndouga.service.IncidentService;
import com.altn72duvernetndouga.service.SurveillanceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.util.List;

/**
 * Pages liées aux incidents : déclaration (F4), détail (F5) et modification (F6).
 */
@Controller
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final EquipementService equipementService;
    private final EvaluationService evaluationService;
    private final SurveillanceService surveillanceService;
    private final Clock horloge;

    public IncidentController(IncidentService incidentService, EquipementService equipementService,
                              EvaluationService evaluationService, SurveillanceService surveillanceService,
                              Clock horloge) {
        this.incidentService = incidentService;
        this.equipementService = equipementService;
        this.evaluationService = evaluationService;
        this.surveillanceService = surveillanceService;
        this.horloge = horloge;
    }

    /**
     * Fournit la liste des équipements à toutes les pages de ce contrôleur,
     * pour remplir la liste déroulante du formulaire de déclaration.
     */
    @ModelAttribute("equipements")
    public List<Equipement> equipements() {
        return equipementService.listerEquipements();
    }

    @GetMapping("/")
    public ResponseEntity<List<Incident>> listerIncidentsParDate() {
        List<Incident> response = incidentService.listerIncidentsParDate();
        return ResponseEntity.ok(response);
    }

    /**
     * Affiche le formulaire de déclaration, vide.
     */
    @GetMapping("/nouveau")
    public String afficherFormulaireDeclaration(Model model) {
        model.addAttribute("formulaire", new DeclarationIncidentForm());
        return "declarer-incident";
    }

    /**
     * Traite le formulaire envoyé : le réaffiche avec les messages d'erreur s'il est invalide,
     * sinon déclare l'incident puis redirige vers son détail, pour éviter un double envoi.
     */
    @PostMapping("/incidents")
    public String declarerIncident(@Valid @ModelAttribute("formulaire") DeclarationIncidentForm formulaire,
                                   BindingResult resultat) {
        if (resultat.hasErrors()) {
            return "declarer-incident";
        }
        Incident incident = incidentService.declarer(formulaire.getEquipementId(), formulaire.getDescription());
        return "redirect:/incidents/" + incident.getId();
    }

    /**
     * Affiche le détail d'un incident : ses informations, sa durée d'ouverture
     * (calculée à partir de l'heure courante), son évaluation éventuelle,
     * et si son prestataire est sous surveillance (PS).
     */
    @GetMapping("/{id}")
    public String afficherDetail(@PathVariable Integer id, Model model) {
        Incident incident = incidentService.trouverParIdentifiant(id);
        model.addAttribute("incident", incident);
        model.addAttribute("dureeEnHeures", incident.dureeOuvertureEnHeures(horloge));
        model.addAttribute("evaluation", evaluationService.trouverPourIncident(incident).orElse(null));
        model.addAttribute("prestataireSousSurveillance",
                surveillanceService.estSousSurveillance(incident.getIdPrestataire()));
        return "incident-detail";
    }

    /**
     * Traite le formulaire envoyé : le réaffiche avec les messages d'erreur s'il est invalide,
     * sinon déclare l'incident puis redirige pour éviter un double envoi en cas de rafraîchissement.
     */
    @PostMapping("/")
    public String declarerIncident(@Valid @ModelAttribute("formulaire") DeclarationIncidentForm formulaire,
                                   BindingResult resultat, RedirectAttributes redirect) {
        if (resultat.hasErrors()) {
            redirect.addFlashAttribute("erreur", "Formulaire invalide.");
            return "redirect:/incident";
        }
        Incident incident = incidentService.declarer(formulaire.getEquipementId(), formulaire.getDescription());
        return "redirect:/incident";
    }

    /**
     * Affiche le formulaire de modification, pré-rempli avec la description actuelle.
     */
    @GetMapping("/incidents/{id}/modifier")
    public String afficherFormulaireModification(@PathVariable Integer id, Model model) {
        Incident incident = incidentService.trouverParIdentifiant(id);
        ModificationIncidentForm formulaire = new ModificationIncidentForm();
        formulaire.setDescription(incident.getDescription());
        model.addAttribute("incident", incident);
        model.addAttribute("formulaire", formulaire);
        return "modifier-incident";
    }

    /**
     * Traite la modification : réaffiche le formulaire s'il est invalide,
     * sinon enregistre la nouvelle description puis redirige vers le détail de l'incident.
     */
    @PostMapping("/incidents/{id}/modifier")
    public String modifierIncident(@PathVariable Integer id,
                                   @Valid @ModelAttribute("formulaire") ModificationIncidentForm formulaire,
                                   BindingResult resultat,
                                   Model model) {
        if (resultat.hasErrors()) {
            model.addAttribute("incident", incidentService.trouverParIdentifiant(id));
            return "modifier-incident";
        }
        incidentService.modifierDescription(id, formulaire.getDescription());
        return "redirect:/incidents/" + id;
    }
}
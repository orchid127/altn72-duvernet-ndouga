package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.exception.EvaluationImpossibleException;
import com.altn72duvernetndouga.formulaire.EvaluationForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Pages liées à l'évaluation d'un prestataire après la résolution d'un incident (F10).
 */
@Controller
public class EvaluationControleur {

    private final EvaluationService evaluationService;
    private final IncidentService incidentService;

    public EvaluationControleur(EvaluationService evaluationService, IncidentService incidentService) {
        this.evaluationService = evaluationService;
        this.incidentService = incidentService;
    }

    /**
     * Affiche le formulaire d'évaluation d'un incident, vide.
     */
    @GetMapping("/incidents/{id}/evaluation")
    public String afficherFormulaireEvaluation(@PathVariable Integer id, Model model) {
        model.addAttribute("incident", incidentService.trouverParIdentifiant(id));
        model.addAttribute("formulaire", new EvaluationForm());
        return "evaluer-incident";
    }

    /**
     * Traite l'évaluation : réaffiche le formulaire s'il est invalide,
     * sinon enregistre l'évaluation puis redirige vers le détail de l'incident.
     */
    @PostMapping("/incidents/{id}/evaluation")
    public String evaluerIncident(@PathVariable Integer id,
                                  @Valid @ModelAttribute("formulaire") EvaluationForm formulaire,
                                  BindingResult resultat,
                                  RedirectAttributes redirect) {
        if (resultat.hasErrors()) {
            redirect.addFlashAttribute("erreur", "Note invalide.");
            return "redirect:/incident";
        }
        try {
            evaluationService.evaluer(id, formulaire.getNote(), formulaire.getCommentaire());
            redirect.addFlashAttribute("message", "Évaluation enregistrée.");
        } catch (EvaluationImpossibleException e) {
            redirect.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/incident";
    }
}
package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.exception.ConfirmationRequiseException;
import com.altn72duvernetndouga.formulaire.AffectationForm;
import com.altn72duvernetndouga.modele.Prestataire;
import com.altn72duvernetndouga.service.IncidentService;
import com.altn72duvernetndouga.service.PrestataireService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Page d'affectation d'un prestataire à un incident (F7), avec la confirmation
 * demandée pour un prestataire sous surveillance (PS5). Réservée au responsable.
 */
@Controller
public class AffectationController {

    private static final String VUE_AFFECTATION = "affecter-incident";

    private final IncidentService incidentService;
    private final PrestataireService prestataireService;

    public AffectationController(IncidentService incidentService, PrestataireService prestataireService) {
        this.incidentService = incidentService;
        this.prestataireService = prestataireService;
    }

    /**
     * Affiche le formulaire d'affectation, avec la liste des prestataires.
     */
    @GetMapping("/incidents/{id}/affectation")
    public String afficherFormulaireAffectation(@PathVariable Integer id, Model model) {
        preparerPage(id, model);
        model.addAttribute("formulaire", new AffectationForm());
        return VUE_AFFECTATION;
    }

    /**
     * Traite l'affectation. Si le prestataire est sous surveillance et que la case de confirmation
     * n'est pas cochée, le service refuse l'affectation : la page se réaffiche avec un message
     * qui demande de confirmer. Sinon, l'affectation est enregistrée et on revient au détail.
     */
    @PostMapping("/incidents/{id}/affectation")
    public String affecterIncident(@PathVariable Integer id,
                                   @Valid @ModelAttribute("formulaire") AffectationForm formulaire,
                                   BindingResult resultat,
                                   Model model) {
        if (resultat.hasErrors()) {
            preparerPage(id, model);
            return VUE_AFFECTATION;
        }
        try {
            Prestataire prestataire = prestataireService.trouverParIdentifiant(formulaire.getPrestataireId());
            incidentService.affecterPrestataireIncident(id, prestataire, formulaire.isConfirmation());
        } catch (ConfirmationRequiseException exception) {
            preparerPage(id, model);
            model.addAttribute("demandeConfirmation", exception.getMessage());
            return VUE_AFFECTATION;
        }
        return "redirect:/incidents/" + id;
    }

    /**
     * Met dans le modèle l'incident et la liste de choix des prestataires, utilisés par la page.
     */
    private void preparerPage(Integer id, Model model) {
        model.addAttribute("incident", incidentService.trouverParIdentifiant(id));
        model.addAttribute("prestataires", prestataireService.listerChoixAffectation());
    }
}
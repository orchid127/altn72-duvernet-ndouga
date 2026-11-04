package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.repository.EquipementRepository;
import com.altn72duvernetndouga.repository.IncidentRepository;
import com.altn72duvernetndouga.service.EquipementService;
import com.altn72duvernetndouga.service.IncidentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageControleur {

    private final EquipementService equipementService;
    private final IncidentService incidentService;

    public PageControleur(EquipementService equipementService,  IncidentService incidentService) {
        this.equipementService = equipementService;
        this.incidentService = incidentService;
    }

    @GetMapping("/connexion")
    public String afficherConnexion(){
        return "connexion";
    }

    @GetMapping("/")
    public String afficherEquipementsl(Model model){
        model.addAttribute("equipements", equipementService.listerEquipements());
        return "equipements";
    }

    @GetMapping("/incident")
    public String afficherIncidents(Model model){
        model.addAttribute("lstIncidents", incidentService.listerIncidentsParDate());
        return "incidents";
    }

    @GetMapping("/dashboard")
    public String afficherDashboard(Model model){
        model.addAttribute("lstIncidents", incidentService.listerIncidentsParDate());
        return "dashboard";
    }
}
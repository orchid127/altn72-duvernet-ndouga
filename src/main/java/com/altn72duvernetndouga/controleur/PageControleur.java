package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.service.EquipementService;
import com.altn72duvernetndouga.service.IncidentService;
import com.altn72duvernetndouga.service.StationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageControleur {

    private final EquipementService equipementService;
    private final IncidentService incidentService;
    private final StationService stationService;

    public PageControleur(EquipementService equipementService,  IncidentService incidentService, StationService stationService) {
        this.equipementService = equipementService;
        this.incidentService = incidentService;
        this.stationService = stationService;
    }

    @GetMapping("/")
    public String afficherEquipements(@RequestParam(required = false) Integer stationId, @RequestParam(required = false) String search, Model model){
        String cleanedSearch = (search == null || search.isBlank()) ? null : search.trim();

        model.addAttribute("equipements", equipementService.filtrerEquipements(stationId, cleanedSearch));
        model.addAttribute("stations", stationService.listerStations());
        model.addAttribute("selectedStationId", stationId);
        model.addAttribute("search", cleanedSearch);
        return "equipements";
    }

    @GetMapping("/connexion")
    public String afficherConnexion(){
        return "connexion";
    }

    @GetMapping("/incident")
    public String afficherIncidents(Model model){
        model.addAttribute("lstIncidents", incidentService.listerIncidentsParDate());
        model.addAttribute("equipements", equipementService.listerEquipements());
        return "incidents";
    }

    @GetMapping("/dashboard")
    public String afficherDashboard(Model model){
        model.addAttribute("lstIncidents", incidentService.listerIncidentsParDate());
        model.addAttribute("stations", stationService.listerStations());
        return "dashboard";
    }
}
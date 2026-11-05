package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.repository.StationRepository;
import com.altn72duvernetndouga.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stations")
public class StationControleur {
    private StationService stationService;

    public StationControleur(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Station>> afficherStations() {
        List<Station> response = stationService.listerStations();
        return ResponseEntity.ok(response);
    }
}

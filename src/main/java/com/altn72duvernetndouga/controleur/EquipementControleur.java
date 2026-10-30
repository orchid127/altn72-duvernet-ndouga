package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.service.EquipementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EquipementControleur {
    private final EquipementService equipementService;

    public EquipementControleur(EquipementService equipementService) {
        this.equipementService = equipementService;
    }

    @GetMapping("/equipements")
    public ResponseEntity<List<Equipement>> afficherEquipements() {
        List<Equipement> response = equipementService.listerEquipements();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipements/{id}")
    public ResponseEntity<Equipement> afficherEquipementParId(@PathVariable int id) {
        Equipement response = equipementService.trouverEquipementParIdentifiant(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/equipements/{idStation}")
    public ResponseEntity<List<Equipement>> afficherEquipementsParStation(@PathVariable int idStation) {
        List<Equipement> response = equipementService.trouverEquipementParStation(idStation);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/equipements")
    public ResponseEntity<Equipement> ajouterEquipement(@RequestBody Equipement equipement) {
        Equipement response =  equipementService.ajouterEquipement(equipement);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<Equipement>(response, HttpStatus.CREATED);
    }
}

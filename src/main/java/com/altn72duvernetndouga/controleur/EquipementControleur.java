package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.service.EquipementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EquipementControleur {
    @Autowired
    private EquipementService equipementService;

    @GetMapping("/equipements")
    public List<Equipement> afficherEquipements() {
        return equipementService.listerEquipements();
    }

    @GetMapping("/equipements/{id}")
    public Equipement afficherEquipementParId(@PathVariable int id) {
        return equipementService.trouverEquipementParIdentifiant(id);
    }

    @GetMapping("/equipements/{idStation}")
    public List<Equipement> afficherEquipementsParStation(@PathVariable int idStation) {
        return equipementService.trouverEquipementParStation(idStation);
    }

    @PostMapping("/equipements")
    public Equipement ajouterEquipement(@RequestBody Equipement equipement) {
        return equipementService.ajouterEquipement(equipement);
    }
}

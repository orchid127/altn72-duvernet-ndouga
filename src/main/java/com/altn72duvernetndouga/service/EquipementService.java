package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.repository.EquipementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipementService {
    @Autowired
    private EquipementRepository equipementRepository;

    public List<Equipement> listerEquipements() {
        return equipementRepository.findAll();
    }

    public Equipement trouverEquipementParIdentifiant(int idEquipement) {
        // checking if the equipment exists in the database
        if (equipementRepository.findById(idEquipement).isPresent()) {
            return equipementRepository.findById(idEquipement).get();
        }
        return null;
    }

    public List<Equipement> trouverEquipementParStation(int idStation) {
        // converts the list of equipments into a stream, then filters it and converts back to a list
        return equipementRepository.findAll().stream()
                .filter(equipement -> equipement.getIdStation().getId() == idStation)
                .toList();
    }

    public Equipement ajouterEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }
}

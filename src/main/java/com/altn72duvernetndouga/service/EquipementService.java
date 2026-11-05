package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.EquipementIntrouvableException;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.repository.EquipementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Cas d'usage liés aux équipements.
 */
@Service
public class EquipementService {

    private final EquipementRepository equipementRepository;

    public EquipementService(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    /**
     * Renvoie tous les équipements, par exemple pour la liste déroulante de déclaration d'un incident.
     */
    public List<Equipement> listerEquipements() {
        return equipementRepository.findAll();
    }

    /**
     * Renvoie les équipements installés dans une station donnée.
     */
    public List<Equipement> trouverEquipementParStation(int idStation) {
        return equipementRepository.findAll().stream()
                .filter(equipement -> equipement.getIdStation().getId() == idStation)
                .toList();
    }

    /**
     * Retrouve un équipement à partir de son identifiant, ou lève une exception s'il n'existe pas.
     */
    public Equipement trouverParIdentifiant(Integer id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new EquipementIntrouvableException(id));
    }

    public List<Equipement> filtrerEquipements(Integer stationId, String search) {
        return equipementRepository.filter(stationId, search);
    }

    public Equipement ajouterEquipement(Equipement equipement) {
        return equipementRepository.save(equipement);
    }
}
package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.EquipementIntrouvableException;
import com.altn72duvernetndouga.modele.Equipement;
import com.altn72duvernetndouga.repository.EquipementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipementService {
    private final EquipementRepository equipementRepository;

    public EquipementService(EquipementRepository equipementRepository) {
        this.equipementRepository = equipementRepository;
    }

    public List<Equipement> listerEquipements(){
        return equipementRepository.findAll();

    }

    public Equipement trouverParIdentifiant(Integer id){
        return equipementRepository.findById(id).orElseThrow(()->new EquipementIntrouvableException(id));
    }
}

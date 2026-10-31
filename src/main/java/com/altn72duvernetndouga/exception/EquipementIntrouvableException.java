package com.altn72duvernetndouga.exception;

public class EquipementIntrouvableException extends ExceptionMetier {
    public EquipementIntrouvableException(Integer id) {
        super("L'équipement " + id + " n'existe pas");
    }
}

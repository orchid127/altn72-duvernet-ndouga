package com.altn72duvernetndouga.exception;

/**
 * Levée quand aucun prestataire ne correspond à l'identifiant demandé.
 */
public class PrestataireIntrouvableException extends ExceptionMetier {

    public PrestataireIntrouvableException(Integer id) {
        super("Le prestataire " + id + " n'existe pas");
    }
}
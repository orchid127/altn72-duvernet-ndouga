package com.altn72duvernetndouga.exception;

/**
 * Levée quand on affecte un incident à un prestataire sous surveillance sans confirmation explicite (PS5).
 * Rien n'est enregistré tant que l'utilisateur n'a pas confirmé.
 */
public class ConfirmationRequiseException extends ExceptionMetier {

    public ConfirmationRequiseException(String nomPrestataire) {
        super("Le prestataire " + nomPrestataire
                + " est sous surveillance : confirmez l'affectation pour l'enregistrer");
    }
}
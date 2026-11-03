package com.altn72duvernetndouga.exception;

/**
 * Levée quand un fichier d'import ne peut pas être traité : fichier vide, illisible ou ligne invalide.
 */
public class ImportImpossibleException extends ExceptionMetier {

    public ImportImpossibleException(String message) {
        super(message);
    }

    /**
     * Garde l'exception d'origine comme cause, pour ne pas perdre la trace technique dans les journaux.
     */
    public ImportImpossibleException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}

package com.altn72duvernetndouga.exception;

public class UtilisateurIntrouvableException extends ExceptionMetier {

    public UtilisateurIntrouvableException(Integer id) {
        super("L'utilisateur " + id + " n'existe pas");
    }
}

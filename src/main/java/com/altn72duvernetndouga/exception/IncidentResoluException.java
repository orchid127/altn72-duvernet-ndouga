package com.altn72duvernetndouga.exception;

public class IncidentResoluException extends ExceptionMetier{
    public IncidentResoluException(Integer id) {
        super("L'incident " + id + " est déjà résolu : il ne peut plus être modifié");
    }
}

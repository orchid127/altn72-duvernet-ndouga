package com.altn72duvernetndouga.exception;

public class IncidentIntrouvableException extends ExceptionMetier{
    public IncidentIntrouvableException(Integer id){
        super("L'incident numéro " + id + " est introuvable" );
    }
}

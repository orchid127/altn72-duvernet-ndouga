package com.altn72duvernetndouga.exception;

public abstract class ExceptionMetier extends RuntimeException {

    protected ExceptionMetier(String message) {
        super(message);
    }
}

package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.dto.ErreurApi;
import com.altn72duvernetndouga.exception.ConfirmationRequiseException;
import com.altn72duvernetndouga.exception.EquipementIntrouvableException;
import com.altn72duvernetndouga.exception.ExceptionMetier;
import com.altn72duvernetndouga.exception.IncidentIntrouvableException;
import com.altn72duvernetndouga.exception.IncidentResoluException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduit les exceptions métier de l'API REST en codes HTTP adaptés, avec un message clair
 * et sans trace technique. Il ne s'applique qu'aux contrôleurs de l'API (@RestController).
 */
@RestControllerAdvice(annotations = RestController.class)
public class GestionnaireErreursApi {

    /**
     * Ressource inexistante : répond 404.
     */
    @ExceptionHandler({IncidentIntrouvableException.class, EquipementIntrouvableException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErreurApi ressourceIntrouvable(ExceptionMetier exception) {
        return new ErreurApi(exception.getMessage());
    }

    /**
     * Opération incompatible avec l'état actuel : incident déjà résolu,
     * ou affectation à un prestataire sous surveillance sans confirmation (PS5). Répond 409.
     */
    @ExceptionHandler({IncidentResoluException.class, ConfirmationRequiseException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErreurApi conflit(ExceptionMetier exception) {
        return new ErreurApi(exception.getMessage());
    }
}
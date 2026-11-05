package com.altn72duvernetndouga.formulaire;

import jakarta.validation.constraints.NotNull;

/**
 * Données saisies dans le formulaire d'affectation d'un prestataire (F7),
 * avec la confirmation exigée pour un prestataire sous surveillance (PS5).
 */
public class AffectationForm {

    @NotNull(message = "Choisissez un prestataire")
    private Integer prestataireId;

    private boolean confirmation;

    public Integer getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(Integer prestataireId) {
        this.prestataireId = prestataireId;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }
}
package com.altn72duvernetndouga.formulaire;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DeclarationIncidentForm {
    @NotNull(message = "Choisissez un équipement")
    private Integer equipementId;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 250, message = "La description doit contenir entre 10 et 250 caractères")
    private String description;

    public Integer getEquipementId() {
        return equipementId;
    }

    public String getDescription() {
        return description;
    }

    public void setEquipementId(Integer equipementId) {
        this.equipementId = equipementId;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

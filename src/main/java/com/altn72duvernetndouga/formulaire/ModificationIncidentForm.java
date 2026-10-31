package com.altn72duvernetndouga.formulaire;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ModificationIncidentForm {
    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 250, message = "La description doit contenir entre 10 et 250 caractères")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

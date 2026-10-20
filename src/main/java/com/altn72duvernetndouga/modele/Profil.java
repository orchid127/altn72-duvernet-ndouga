package com.altn72duvernetndouga.modele;

public enum Profil {
    RESPONSABLE_EXPLOITATION("Responsable d'exploitation"),
    AGENT_STATION("Agent de station");

    private final String libelle;

    Profil(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

}

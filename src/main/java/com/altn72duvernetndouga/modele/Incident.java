package com.altn72duvernetndouga.modele;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incident", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_equipement", nullable = false)
    private Equipement idEquipement;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Column(name = "date_ouverture", nullable = false)
    private Instant dateOuverture;

    @Lob
    @Column(name = "statut", nullable = false)
    private String statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestataire")
    private Prestataire idPrestataire;

    @Column(name = "date_resolution")
    private Instant dateResolution;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipement getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(Equipement idEquipement) {
        this.idEquipement = idEquipement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(Instant dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Prestataire getIdPrestataire() {
        return idPrestataire;
    }

    public void setIdPrestataire(Prestataire idPrestataire) {
        this.idPrestataire = idPrestataire;
    }

    public Instant getDateResolution() {
        return dateResolution;
    }

    public void setDateResolution(Instant dateResolution) {
        this.dateResolution = dateResolution;
    }

}
package com.altn72duvernetndouga.modele;

import jakarta.persistence.*;

@Entity
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipement", nullable = false)
    private Integer id;

    @Column(name = "reference", nullable = false, length = 100)
    private String reference;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @Lob
    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_station", nullable = false)
    private Station idStation;

    @Lob
    @Column(name = "etat_fontionnement", nullable = false)
    private String etatFontionnement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Station getIdStation() {
        return idStation;
    }

    public void setIdStation(Station idStation) {
        this.idStation = idStation;
    }

    public String getEtatFontionnement() {
        return etatFontionnement;
    }

    public void setEtatFontionnement(String etatFontionnement) {
        this.etatFontionnement = etatFontionnement;
    }

}
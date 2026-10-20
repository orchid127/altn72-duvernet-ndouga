package com.altn72duvernetndouga.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Utilisateur {
    @Id
    @Column(name = "id_connexion", nullable = false)
    private Integer id;

    @Column(name = "mot_de_passe", nullable = false, length = 100, unique = true)
    private String motDePasse;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "profil", length = 100)
    private String profil;

    protected Utilisateur() {
    }

    public Utilisateur(Integer id, String motDePasse, String prenom, String profil) {
        this.id = id;
        this.motDePasse = motDePasse;
        this.prenom = prenom;
        this.profil = profil;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

}
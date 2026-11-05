package com.altn72duvernetndouga.modele;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluation", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_incident", nullable = false)
    private Incident idIncident;

    @Column(name = "note", nullable = false)
    private Integer note;

    @Column(name = "commentaire", length = 100)
    private String commentaire;

    @Column(name = "date_evaluation", nullable = false)
    private LocalDate dateEvaluation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluation")
    private Incident incident;

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    protected Evaluation(){

    }

    public Evaluation(Incident idIncident, Integer note, String commentaire, LocalDate dateEvaluation) {
        this.idIncident = idIncident;
        this.note = note;
        this.commentaire = commentaire;
        this.dateEvaluation = dateEvaluation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Incident getIdIncident() {
        return idIncident;
    }

    public void setIdIncident(Incident idIncident) {
        this.idIncident = idIncident;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDate getDateEvaluation() {
        return dateEvaluation;
    }

    public void setDateEvaluation(LocalDate dateEvaluation) {
        this.dateEvaluation = dateEvaluation;
    }

}
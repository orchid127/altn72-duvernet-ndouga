package com.altn72duvernetndouga.dto;

/**
 * Une ligne de la liste de choix du formulaire d'affectation : l'identifiant du prestataire
 * et le libellé affiché, qui contient « (sous surveillance) » si c'est le cas.
 */
public record ChoixPrestataire(Integer id, String libelle) {
}
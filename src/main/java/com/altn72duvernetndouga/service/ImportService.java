package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.exception.ImportImpossibleException;
import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Importe des stations depuis un fichier CSV (lot alpha, piste 9).
 */
@Service
public class ImportService {

    private final StationRepository stationRepository;

    public ImportService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    /**
     * Crée une station par ligne du fichier, en ignorant la première ligne (les en-têtes)
     * et les lignes vides. Renvoie le nombre de stations créées.
     * L'annotation Transactional rend l'import atomique (RG10) : si une seule station ne peut pas être enregistrée,
     * aucune ne l'est. Le try-with-resources ferme le fichier automatiquement, même en cas d'erreur.
     */
    @Transactional
    public int importerStations(MultipartFile fichier) {
        if (fichier.isEmpty()) {
            throw new ImportImpossibleException("Le fichier envoyé est vide");
        }
        try (BufferedReader lecteur = new BufferedReader(
                new InputStreamReader(fichier.getInputStream(), StandardCharsets.UTF_8))) {
            return enregistrerStations(lecteur.lines().toList());
        } catch (IOException e) {
            throw new ImportImpossibleException("Le fichier n'a pas pu être lu", e);
        }
    }

    /**
     * Enregistre une station pour chaque nom, à partir de la deuxième ligne.
     * Refuse le fichier s'il ne contient aucune station.
     */
    private int enregistrerStations(List<String> lignes) {
        int nombreDeStations = 0;
        for (int numero = 1; numero < lignes.size(); numero++) {
            String nom = lignes.get(numero).trim();
            if (!nom.isEmpty()) {
                Station station = new Station();
                station.setNomStation(nom);
                stationRepository.save(station);
                nombreDeStations++;
            }
        }
        if (nombreDeStations == 0) {
            throw new ImportImpossibleException("Le fichier ne contient aucune station à importer");
        }
        return nombreDeStations;
    }
}

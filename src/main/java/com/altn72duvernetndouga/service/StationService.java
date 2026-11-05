package com.altn72duvernetndouga.service;

import com.altn72duvernetndouga.modele.Station;
import com.altn72duvernetndouga.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> listerStations() {
        return stationRepository.findAll();
    }
}

package com.altn72duvernetndouga.repository;

import com.altn72duvernetndouga.modele.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipementRepository extends JpaRepository<Equipement, Integer> {
    @Query("SELECT e FROM Equipement e WHERE (:stationId IS NULL OR e.idStation.id = :stationId) " +
            "AND (:search IS NULL OR LOWER(e.reference) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Equipement> filter(@Param("stationId") Integer stationId,
                            @Param("search") String search);
}

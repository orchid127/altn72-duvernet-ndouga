package com.altn72duvernetndouga.repository;

import com.altn72duvernetndouga.modele.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentRepository extends JpaRepository<Incident, Integer> {
}

package com.paf.fahrwerk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.paf.fahrwerk.model.SensorFehler;

public interface SensorFehlerRepository extends JpaRepository<SensorFehler, Long> {

    // Hol die neuesten 5 Fehler basierend auf dem Zeitstempel
    List<SensorFehler> findTop5ByOrderByZeitstempelDesc();

}

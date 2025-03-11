package com.paf.fahrwerk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.paf.fahrwerk.model.SensorFehler;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface SensorFehlerRepository extends JpaRepository<SensorFehler, Long> {

    // Hol die neuesten 5 Fehler basierend auf dem Zeitstempel
    List<SensorFehler> findTop5ByOrderByZeitstempelDesc();

    @Query("SELECT COUNT(sf) FROM SensorFehler sf WHERE sf.sensorId = :sensorId AND sf.zeitstempel > :timeLimit")
    long countRecentErrors(@Param("sensorId") Long sensorId, @Param("timeLimit") LocalDateTime timeLimit);

    @Query("SELECT COUNT(DISTINCT sf.sensorId) FROM SensorFehler sf WHERE sf.zeitstempel > :timeLimit")
    long countDistinctErrorSensors(@Param("timeLimit") LocalDateTime timeLimit);

}

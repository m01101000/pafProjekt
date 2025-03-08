package com.paf.fahrwerk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.paf.fahrwerk.model.SensorFehler;

public interface SensorFehlerRepository extends JpaRepository<SensorFehler, Long> {
}

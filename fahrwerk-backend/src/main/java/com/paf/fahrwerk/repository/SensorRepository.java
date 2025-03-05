package com.paf.fahrwerk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paf.fahrwerk.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
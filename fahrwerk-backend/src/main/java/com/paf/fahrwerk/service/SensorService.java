package com.paf.fahrwerk.service;

import org.springframework.stereotype.Service;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.repository.SensorRepository;

import java.util.List;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    public Sensor getSensorById(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }    
}
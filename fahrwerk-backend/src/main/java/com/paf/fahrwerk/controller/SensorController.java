package com.paf.fahrwerk.controller;

import org.springframework.web.bind.annotation.*;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.service.SensorService;

import java.util.List;

@RestController
@RequestMapping("/api/sensoren")
public class SensorController {
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public List<Sensor> getAllSensors() {
        return sensorService.getAllSensors();
    }
}

package com.paf.fahrwerk1.service;

import static org.junit.jupiter.api.Assertions.*;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.service.SensorPruefService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SensorPruefServiceTest {

    @Autowired
    private SensorPruefService sensorPruefService;  // ✅ Spring Boot verwaltet die Instanz automatisch

    @Test
    void testGueltigerSensorwert() {
        Sensor sensor = new Sensor(1L, "Vorne Links", 52.0);
        assertTrue(sensorPruefService.istSensorwertGueltig(sensor));
    }

    @Test
    void testUngueltigerSensorwert() {
        Sensor sensor = new Sensor(1L, "Vorne Links", 48.0);
        assertFalse(sensorPruefService.istSensorwertGueltig(sensor));
    }
}

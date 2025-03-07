package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class SensorSimulator {
    private static final Logger logger = LoggerFactory.getLogger(SensorSimulator.class);
    private final SensorPruefService sensorPruefService;
    private final List<Sensor> sensoren = Collections.synchronizedList(new ArrayList<>());
    private final Random random = new Random();

    public SensorSimulator(SensorPruefService sensorPruefService) {
        this.sensorPruefService = sensorPruefService;
        initialisiereSensoren();
    }

    private void initialisiereSensoren() {
        sensoren.clear();
        sensoren.add(new Sensor(1L, "Vorne Links", 0));
        sensoren.add(new Sensor(2L, "Vorne Rechts", 0));
        sensoren.add(new Sensor(3L, "Hinten Links", 0));
        sensoren.add(new Sensor(4L, "Hinten Rechts", 0));
    }

    @Scheduled(fixedRate = 500)
    public void aktualisiereSensorDaten() {
        logger.info("--- Sensordaten aktualisieren ---");
        aktualisiereSensorGrenzwerte();

        for (Sensor sensor : sensoren) {
            sensor.setHoehe(getRandomHoehe());

            boolean gueltig = sensorPruefService.istSensorwertGueltig(sensor);
            if (!gueltig) {
                logger.warn("❌ Ungültiger Sensorwert: Sensor {} ({} mm)", sensor.getPosition(), sensor.getHoehe());
                double korrigierteHoehe = (sensor.getMinWert() + sensor.getMaxWert()) / 2;
                sensor.setHoehe(korrigierteHoehe);
                logger.info("🔧 Sensorwert korrigiert auf: {} mm", korrigierteHoehe);
            }
        }
    }

    private void aktualisiereSensorGrenzwerte() {
        for (Sensor sensor : sensoren) {
            sensor.setMinWert(sensorPruefService.getMinWert());
            sensor.setMaxWert(sensorPruefService.getMaxWert());
        }
    }


    private double getRandomHoehe() {
        return sensorPruefService.getMinWert()
                + random.nextDouble() * (sensorPruefService.getMaxWert() - sensorPruefService.getMinWert());
    }

    public List<Sensor> getSensoren() {
        return sensoren;
    }
}

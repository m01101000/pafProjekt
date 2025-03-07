package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SensorSimulator {
    private static final Logger logger = LoggerFactory.getLogger(SensorSimulator.class);
    private final SensorWebSocketService webSocketService;
    private final SensorPruefService sensorPruefService;
    private final List<Sensor> sensoren;
    private final Random random = new Random();

    public SensorSimulator(SensorWebSocketService webSocketService, SensorPruefService sensorPruefService) {
        this.webSocketService = webSocketService;
        this.sensorPruefService = sensorPruefService;
        this.sensoren = new ArrayList<>();

        // Sensoren-Liste initialisieren
        initialisiereSensoren();
    }

    private void initialisiereSensoren() {
        sensoren.clear(); // Vorherige Sensoren entfernen, um doppelte Einträge zu vermeiden
        sensoren.add(new Sensor(1L, "Vorne Links", getRandomHoehe()));
        sensoren.add(new Sensor(2L, "Vorne Rechts", getRandomHoehe()));
        sensoren.add(new Sensor(3L, "Hinten Links", getRandomHoehe()));
        sensoren.add(new Sensor(4L, "Hinten Rechts", getRandomHoehe()));

        for (Sensor sensor : sensoren) {
            logger.info("✅ Initialisierte Sensor {} ({}): Höhe gesetzt auf {} mm | Erwartet: [{} - {}]", 
                        sensor.getId(), sensor.getPosition(), sensor.getHoehe(),
                        sensorPruefService.getMinWert(), sensorPruefService.getMaxWert());
        }
    }

    @Scheduled(fixedRate = 2000) // Alle 2 Sekunden neue Sensordaten generieren
    public void sendRandomSensorData() {
        logger.info("--- Prüfbericht der Sensordaten ---");
    
        for (Sensor sensor : sensoren) {
            sensor.setHoehe(getRandomHoehe());
    
            // Debugging: Direkt loggen, welcher Prüfmodus aktiv ist
            logger.info("🔍 Aktueller Prüfmodus: {}", sensorPruefService.getAktuellerPruefmodus());
            logger.info("🔹 Sensor {} | Position: {} | Höhe: {} mm | Erwartet: [{} - {}]",
                    sensor.getId(), sensor.getPosition(), sensor.getHoehe(),
                    sensorPruefService.getMinWert(), sensorPruefService.getMaxWert());
    
            boolean gueltig = sensorPruefService.istSensorwertGueltig(sensor);
            if (!gueltig) {
                logger.error("❌ FEHLER: Sensor {} außerhalb des gültigen Bereichs! Höhe: {} mm | Erwartet: [{} - {}]",
                        sensor.getId(), sensor.getHoehe(),
                        sensorPruefService.getMinWert(), sensorPruefService.getMaxWert());
    
                double korrigierteHoehe = (sensorPruefService.getMinWert() + sensorPruefService.getMaxWert()) / 2;
                sensor.setHoehe(korrigierteHoehe);
                logger.info("🔧 SYSTEM-REAKTION: Sensor {} angepasst auf neue Höhe: {} mm", sensor.getId(), sensor.getHoehe());
            }
    
            webSocketService.sendSensorData(sensor);
        }
    
        logger.info("--- Ende des Prüfberichts ---\n");
    }
    

    private double getRandomHoehe() {
        return sensorPruefService.getMinWert() + random.nextDouble() * (sensorPruefService.getMaxWert() - sensorPruefService.getMinWert());
    }
}

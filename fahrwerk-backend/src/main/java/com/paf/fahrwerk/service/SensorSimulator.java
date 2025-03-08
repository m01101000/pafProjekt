package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.model.SensorFehler;
import com.paf.fahrwerk.repository.SensorFehlerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private SensorFehlerRepository sensorFehlerRepository;

    @Scheduled(fixedRate = 500)
    public void aktualisiereSensorDaten() {
        logger.info("--- Sensorwerte aktualisieren ---");
        aktualisiereSensorGrenzwerte();

        for (Sensor sensor : sensoren) {
            sensor.setHoehe(getRandomHoehe());

            if (!sensorPruefService.istSensorwertGueltig(sensor)) {
                logger.error("❌ Fehler erkannt: Sensor: {} ({} mm)", sensor.getPosition(), sensor.getHoehe());

                SensorFehler fehler = new SensorFehler();
                fehler.setSensorId(sensor.getId());
                fehler.setPosition(sensor.getPosition());
                fehler.setGemesseneHoehe(sensor.getHoehe());
                fehler.setMinWert(sensorPruefService.getMinWert());
                fehler.setMaxWert(sensorPruefService.getMaxWert());
                fehler.setZeitstempel(LocalDateTime.now());

                sensorFehlerRepository.save(fehler);
            }
        }
    }

    public void alleFehlerInConsoleAusgeben() {
        List<SensorFehler> alleFehler = sensorFehlerRepository.findAll();
    
        logger.info("📜 Gespeicherte Sensordaten-Fehler:");
        for (SensorFehler fehler : alleFehler) {
            logger.info("SensorId: {}, Position: {}, Höhe: {} mm, Grenzwerte: [{} - {}], Zeitpunkt: {}",
                fehler.getSensorId(),
                fehler.getPosition(),
                fehler.getGemesseneHoehe(),
                fehler.getMinWert(),
                fehler.getMaxWert(),
                fehler.getZeitstempel());
        }
    }

    private void aktualisiereSensorGrenzwerte() {
        for (Sensor sensor : sensoren) {
            sensor.setMinWert(sensorPruefService.getMinWert());
            sensor.setMaxWert(sensorPruefService.getMaxWert());
        }
    }

    private double getRandomHoehe() {
        double min = sensorPruefService.getMinWert();
        double max = sensorPruefService.getMaxWert();
        double hoehe;
    
        // Wahrscheinlichkeit festlegen (z.B. 20% außerhalb des Bereichs)
        if(random.nextDouble() < 0.01) {
            // außerhalb des erlaubten Bereichs generieren
            hoehe = (random.nextBoolean())
                ? min - random.nextDouble() * 5    // unter minWert
                : max + random.nextDouble() * 5;   // über maxWert
        } else {
            hoehe = min + random.nextDouble() * (max - min); // im Bereich
        }
    
        return Math.round(hoehe * 100.0) / 100.0;
    }
    
    public List<Sensor> getSensoren() {
        return sensoren;
    }
}

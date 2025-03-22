package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.model.SensorFehler;
import com.paf.fahrwerk.patterns.Pruefstrategie;
import com.paf.fahrwerk.repository.SensorFehlerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SensorPruefService {
    private static final Logger logger = LoggerFactory.getLogger(SensorPruefService.class);

    private final ApplicationContext context;
    private Pruefstrategie pruefstrategie;

    @Autowired
    private SensorFehlerRepository sensorFehlerRepository;

    public void pruefeUndSpeichereFehler(Sensor sensor) {
        if (!pruefstrategie.pruefeSensorwert(sensor)) {
            logger.error("❌ Fehler erkannt: Sensor: {} ({} mm)", sensor.getPosition(), sensor.getHoehe());
            SensorFehler fehler = new SensorFehler();
            fehler.setSensorId(sensor.getId());
            fehler.setPosition(sensor.getPosition());
            fehler.setGemesseneHoehe(sensor.getHoehe());
            fehler.setMinWert(getMinWert());
            fehler.setMaxWert(getMaxWert());
            fehler.setZeitstempel(java.time.LocalDateTime.now());

            sensorFehlerRepository.save(fehler);
        }
    }

    @Autowired
    public SensorPruefService(ApplicationContext context) {
        this.context = context;
        this.pruefstrategie = context.getBean("standardPruefung", Pruefstrategie.class);
    }

    public boolean istSensorwertGueltig(Sensor sensor) {
        return pruefstrategie.pruefeSensorwert(sensor);
    }

    public void setPruefmodus(String modus) {
        logger.info("🔍 Prüfmodus wird geändert zu: {}", modus);
        
        try {
            pruefstrategie = context.getBean(modus, Pruefstrategie.class);
            logger.info("✅ Prüfmodus erfolgreich geändert zu: {}", modus);
        } catch (Exception e) {
            logger.error("❌ Ungültiger Prüfmodus: {}", modus);
            throw new IllegalArgumentException("Ungültiger Prüfmodus: " + modus);
        }
    }

    public double getMinWert() {
        return pruefstrategie.getMinWert();
    }

    public double getMaxWert() {
        return pruefstrategie.getMaxWert();
    }

    public String getAktuellerPruefmodus() {
        return pruefstrategie.getClass().getSimpleName();
    }
}

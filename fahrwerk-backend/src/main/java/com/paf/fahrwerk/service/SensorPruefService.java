package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.patterns.Pruefstrategie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorPruefService {
    private static final Logger logger = LoggerFactory.getLogger(SensorPruefService.class);

    private final ApplicationContext context;
    private Pruefstrategie pruefstrategie;
    private final List<Sensor> sensoren;

    @Autowired
    public SensorPruefService(ApplicationContext context, List<Sensor> sensoren) {
        this.context = context;
        this.sensoren = sensoren;
        this.pruefstrategie = context.getBean("standardPruefung", Pruefstrategie.class); // Standard setzen
        aktualisiereSensorGrenzwerte();
    }

    public boolean istSensorwertGueltig(Sensor sensor) {
        return pruefstrategie.pruefeSensorwert(sensor);
    }

    public void setPruefmodus(String modus) {
        logger.info("🔍 VERSUCH, Prüfmodus zu ändern: {}", modus);
        
        try {
            Pruefstrategie neueStrategie = context.getBean(modus, Pruefstrategie.class);
            this.pruefstrategie = neueStrategie;
            logger.info("✅ Prüfmodus erfolgreich geändert zu: {}", modus);
            
            aktualisiereSensorGrenzwerte();
        } catch (Exception e) {
            logger.error("❌ Fehler beim Ändern des Prüfmodus: Ungültiger Modus {}", modus);
        }
    }
    
    
    private void aktualisiereSensorGrenzwerte() {
        for (Sensor sensor : sensoren) {
            pruefstrategie.aktualisiereSensorWerte(sensor);
            logger.info("🔄 Sensor {} ({}): Neuer Wertebereich: [{} - {}]", 
                        sensor.getId(), sensor.getPosition(), sensor.getMinWert(), sensor.getMaxWert());
        }
    }

    public double getMinWert() {
        return pruefstrategie.getMinWert();
    }

    public double getMaxWert() {
        return pruefstrategie.getMaxWert();
    }

    public String getAktuellerPruefmodus() {
        return pruefstrategie.getClass().getSimpleName(); // Gibt den Namen der aktiven Strategie zurück
    }
    
}

package com.paf.fahrwerk.controller;

import org.springframework.web.bind.annotation.*;
import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.model.SensorFehler;
import com.paf.fahrwerk.repository.SensorFehlerRepository;
import com.paf.fahrwerk.service.SensorPruefService;
import com.paf.fahrwerk.service.SensorSimulator;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/sensoren")
public class SensorController {

    private final SensorPruefService sensorPruefService;
    private final SensorSimulator sensorSimulator;
    private final SensorFehlerRepository sensorFehlerRepository;

    public SensorController(SensorPruefService sensorPruefService, SensorSimulator sensorSimulator, SensorFehlerRepository sensorFehlerRepository) {
        this.sensorPruefService = sensorPruefService;
        this.sensorSimulator = sensorSimulator;
        this.sensorFehlerRepository = sensorFehlerRepository;
    }

    @GetMapping("/aktuelle-daten")
    public List<Sensor> getAktuelleSensordaten() {
        return sensorSimulator.getSensoren();
    }

    @GetMapping("/pruefmodus/{modus}")
    public ResponseEntity<String> setPruefmodus(@PathVariable String modus) {
        try {
            sensorPruefService.setPruefmodus(modus);
            return ResponseEntity.ok("✅ Prüfmodus geändert zu: " + modus);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/pruefmodus")
    public ResponseEntity<String> getAktuellerPruefmodus() {
        return ResponseEntity.ok("Aktueller Prüfmodus: " + sensorPruefService.getAktuellerPruefmodus());
    }

    // REST-Endpunkt für den aktuellen Wertebereich
    @GetMapping("/wertebereich")
    public ResponseEntity<String> getWertebereich() {
        return ResponseEntity.ok("Aktueller Wertebereich: [" 
          + sensorPruefService.getMinWert() + " - " + sensorPruefService.getMaxWert() + "]");
    }

    // Sensordaten abfragen und zurückgeben
    @GetMapping("/alle")
    public List<Sensor> getAlleSensoren() {
        return sensorSimulator.getSensoren();
    }

    @GetMapping("/fehler")
    public List<SensorFehler> getLetzteFehler() {
        return sensorFehlerRepository.findTop5ByOrderByZeitstempelDesc();
    }

    @DeleteMapping("/fehler")
    public ResponseEntity<String> deleteAllSensorFehler() {
        sensorFehlerRepository.deleteAll();
        return ResponseEntity.ok("🗑️ Alle Sensor-Fehler wurden gelöscht!");
    }


}

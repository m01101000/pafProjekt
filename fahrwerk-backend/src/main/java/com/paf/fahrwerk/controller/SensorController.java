package com.paf.fahrwerk.controller;

import org.springframework.web.bind.annotation.*;
import com.paf.fahrwerk.model.Sensor;
import com.paf.fahrwerk.model.SensorFehler;
import com.paf.fahrwerk.repository.SensorFehlerRepository;
import com.paf.fahrwerk.service.SensorPruefService;
import com.paf.fahrwerk.service.SensorSimulator;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/fehler/haeufige")
    public List<String> getHaeufigeFehler() {
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(5);
        List<SensorFehler> alleFehler = sensorFehlerRepository.findAll();

        List<String> fehlerListe = alleFehler.stream()
            .filter(fehler -> sensorFehlerRepository.countRecentErrors(fehler.getSensorId(), timeLimit) > 3)
            .map(fehler -> "⚠️ Sensor " + fehler.getPosition() + " hatte " +
                    sensorFehlerRepository.countRecentErrors(fehler.getSensorId(), timeLimit) + " Fehler in den letzten 5 Minuten.")
            .distinct()
            .collect(Collectors.toList());

        // ✅ Falls keine Fehler gefunden wurden, füge eine Standardmeldung hinzu
        if (fehlerListe.isEmpty()) {
            fehlerListe.add("✅ Keine häufigen Sensorfehler erkannt.");
        }

        return fehlerListe;
    }

    @GetMapping("/fehler/korrelation")
    public ResponseEntity<String> getFehlerKorrelation() {
        LocalDateTime timeLimit = LocalDateTime.now().minusMinutes(5);
        long affectedSensors = sensorFehlerRepository.countDistinctErrorSensors(timeLimit);

        if (affectedSensors > 2) {
            return ResponseEntity.ok("🚨 KRITISCHE WARNUNG: " + affectedSensors + " Sensoren haben Fehler registriert!");
        } else {
            return ResponseEntity.ok("✅ Keine kritischen Sensorwarnungen vorhanden.");
        }
    }

}

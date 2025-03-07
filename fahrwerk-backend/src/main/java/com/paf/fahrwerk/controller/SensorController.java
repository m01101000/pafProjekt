package com.paf.fahrwerk.controller;

import org.springframework.web.bind.annotation.*;

import com.paf.fahrwerk.service.SensorPruefService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/sensoren")
public class SensorController {
    private final SensorPruefService sensorPruefService;

    public SensorController(SensorPruefService sensorPruefService) {
        this.sensorPruefService = sensorPruefService;
    }

    @GetMapping("/pruefmodus/{modus}")
    public ResponseEntity<String> setPruefmodus(@PathVariable String modus) {
        try {
            sensorPruefService.setPruefmodus(modus);
            return ResponseEntity.ok("✅ Prüfmodus geändert zu: " + modus);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Ungültiger Prüfmodus: " + modus);
        }
    }

    @GetMapping("/pruefmodus")
    public ResponseEntity<String> getAktuellerPruefmodus() {
        return ResponseEntity.ok("Aktueller Prüfmodus: " + sensorPruefService.getAktuellerPruefmodus());
    }

}

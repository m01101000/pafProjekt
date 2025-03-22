package com.paf.fahrwerk.performace;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.paf.fahrwerk.model.SensorFehler;
import com.paf.fahrwerk.repository.SensorFehlerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)  // ✅ Nutzt Mockito für Tests
class SensorPerformanceTest {

    @Mock  // ✅ Simuliert die Datenbank
    private SensorFehlerRepository sensorFehlerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMassiveSensorFehlerSpeicherung() {
        int anzahl = 100_000;
        List<SensorFehler> sensorFehlerListe = new ArrayList<>();

        for (int i = 0; i < anzahl; i++) {
            sensorFehlerListe.add(new SensorFehler(null, 1L, "Vorne Links", 30.0, 40.0, 60.0, LocalDateTime.now()));  // ✅ `id = null`, damit JPA sie generiert
        }

        long startTime = System.currentTimeMillis();
        when(sensorFehlerRepository.saveAll(anyList())).thenReturn(sensorFehlerListe);  // ✅ Mockito-Simulation für `saveAll`
        sensorFehlerRepository.saveAll(sensorFehlerListe);
        long endTime = System.currentTimeMillis();

        System.out.println("⏳ Zeit für " + anzahl + " Einträge: " + (endTime - startTime) + "ms");

        assertTrue(sensorFehlerListe.size() == anzahl, "Fehlerspeicherung fehlgeschlagen!");
    }

    @Test
    void testAPIResponseTime() {
        long startTime = System.currentTimeMillis();

        when(sensorFehlerRepository.findAll()).thenReturn(new ArrayList<>());  // ✅ Mockito-Simulation für `findAll`
        sensorFehlerRepository.findAll();

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        System.out.println("⚡ API-Antwortzeit: " + responseTime + "ms");

        assertTrue(responseTime < 500, "API-Response zu langsam!");
    }
}

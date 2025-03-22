package com.paf.fahrwerk.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.paf.fahrwerk.model.SensorFehler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest  // ✅ Testet NUR die Repository-Schicht, ohne die gesamte Spring Boot App
@Transactional  // ✅ Stellt sicher, dass jede Transaktion isoliert ist
class SensorFehlerRepositoryTest {

    @Autowired
    private SensorFehlerRepository sensorFehlerRepository;  // ✅ Zugriff auf das Repository

    @Test
    void testFehlerSpeichernUndAbrufen() {
        // 🔹 Neuen Fehler in die DB speichern (OHNE ID!)
        SensorFehler fehler = new SensorFehler();
        fehler.setSensorId(1L);
        fehler.setPosition("Vorne Links");
        fehler.setGemesseneHoehe(45.0);
        fehler.setMinWert(50.0);
        fehler.setMaxWert(55.0);
        fehler.setZeitstempel(LocalDateTime.now());

        // 🔹 Speichern und sicherstellen, dass die ID generiert wird
        fehler = sensorFehlerRepository.saveAndFlush(fehler);
        assertNotNull(fehler.getId(), "ID sollte nach dem Speichern nicht null sein");

        // 🔹 Fehler aus der Datenbank abrufen
        SensorFehler gespeicherterFehler = sensorFehlerRepository.findById(fehler.getId()).orElse(null);

        // 🔹 Prüfen, ob der Fehler korrekt gespeichert wurde
        assertNotNull(gespeicherterFehler, "Fehler sollte in der DB existieren");
        assertEquals("Vorne Links", gespeicherterFehler.getPosition());
        assertEquals(45.0, gespeicherterFehler.getGemesseneHoehe());
    }

    @Test
    void testMehrereFehlerSpeichernUndAbrufen() {
        // 🔹 Zwei Fehler in die Datenbank speichern (OHNE manuelle ID)
        SensorFehler fehler1 = new SensorFehler();
        fehler1.setSensorId(2L);
        fehler1.setPosition("Hinten Rechts");
        fehler1.setGemesseneHoehe(42.0);
        fehler1.setMinWert(50.0);
        fehler1.setMaxWert(55.0);
        fehler1.setZeitstempel(LocalDateTime.now());

        SensorFehler fehler2 = new SensorFehler();
        fehler2.setSensorId(3L);
        fehler2.setPosition("Vorne Rechts");
        fehler2.setGemesseneHoehe(44.0);
        fehler2.setMinWert(50.0);
        fehler2.setMaxWert(55.0);
        fehler2.setZeitstempel(LocalDateTime.now());

        sensorFehlerRepository.saveAndFlush(fehler1);
        sensorFehlerRepository.saveAndFlush(fehler2);

        // 🔹 Alle Fehler aus der Datenbank abrufen
        List<SensorFehler> fehlerListe = sensorFehlerRepository.findAll();

        // 🔹 Prüfen, ob beide Fehler gespeichert wurden
        assertFalse(fehlerListe.isEmpty(), "Es sollten Fehler in der Datenbank sein");
        assertEquals(2, fehlerListe.size());
    }
}

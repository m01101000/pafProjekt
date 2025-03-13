package com.paf.fahrwerk.patterns;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.paf.fahrwerk.model.Sensor;

class PruefstrategieTest {

    private Pruefstrategie standardPruefung;
    private Pruefstrategie sportPruefung;
    private Pruefstrategie comfortPruefung;
    private Pruefstrategie offroadPruefung;

    @BeforeEach
    void setUp() {
        standardPruefung = new StandardPruefung();
        sportPruefung = new SportPruefung();
        comfortPruefung = new ComfortPruefung();
        offroadPruefung = new OffroadPruefung();
    }

    @Test
    void testStandardPruefungWertebereich() {
        assertEquals(50.0, standardPruefung.getMinWert(), "StandardPrüfung MinWert sollte 50.0 sein");
        assertEquals(60.0, standardPruefung.getMaxWert(), "StandardPrüfung MaxWert sollte 60.0 sein");
    }

    @Test
    void testSportPruefungWertebereich() {
        assertEquals(50.0, sportPruefung.getMinWert(), "SportPrüfung MinWert sollte 50.0 sein");
        assertEquals(55.0, sportPruefung.getMaxWert(), "SportPrüfung MaxWert sollte 55.0 sein");
    }

    @Test
    void testComfortPruefungWertebereich() {
        assertEquals(40.0, comfortPruefung.getMinWert(), "EcoPrüfung MinWert sollte 40.0 sein");
        assertEquals(60.0, comfortPruefung.getMaxWert(), "EcoPrüfung MaxWert sollte 60.0 sein");
    }

    @Test
    void testOffroadPruefungWertebereich() {
        assertEquals(45.0, offroadPruefung.getMinWert(), "EcoPrüfung MinWert sollte 45.0 sein");
        assertEquals(60.0, offroadPruefung.getMaxWert(), "EcoPrüfung MaxWert sollte 60.0 sein");
    }


    @Test
    void testStandardPruefungGueltigerWert() {
        assertTrue(standardPruefung.pruefeSensorwert(new Sensor(1L, "Vorne Links", 55.0)), 
                "55.0 sollte in StandardPrüfung gültig sein");
        assertFalse(standardPruefung.pruefeSensorwert(new Sensor(2L, "Vorne Links", 65.0)), 
                "65.0 sollte in StandardPrüfung ungültig sein");
    }

    @Test
    void testSportPruefungGueltigerWert() {
        assertTrue(sportPruefung.pruefeSensorwert(new Sensor(1L, "Hinten Rechts", 55.0)), 
                "45.0 sollte in SportPrüfung gültig sein");
        assertFalse(sportPruefung.pruefeSensorwert(new Sensor(2L, "Hinten Rechts", 60.0)), 
                "60.0 sollte in SportPrüfung ungültig sein");
    }

    @Test
    void testComfortPruefungGueltigerWert() {
        assertTrue(comfortPruefung.pruefeSensorwert(new Sensor(1L, "Vorne Rechts", 60.0)), 
                "60.0 sollte in ComfortPrüfung gültig sein");
        assertFalse(comfortPruefung.pruefeSensorwert(new Sensor(2L, "Vorne Rechts", 39.0)), 
                "50.0 sollte in ComfortPrüfung ungültig sein");
    }

    @Test
    void testOffroadPruefungGueltigerWert() {
        assertTrue(offroadPruefung.pruefeSensorwert(new Sensor(1L, "Vorne Rechts", 60.0)), 
                "60.0 sollte in OffroadPrüfung gültig sein");
        assertFalse(offroadPruefung.pruefeSensorwert(new Sensor(2L, "Vorne Rechts", 44.0)), 
                "50.0 sollte in OffroadPrüfung ungültig sein");
    }
}

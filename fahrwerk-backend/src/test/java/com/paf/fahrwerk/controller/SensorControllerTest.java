package com.paf.fahrwerk.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.paf.fahrwerk.service.SensorPruefService;
import com.paf.fahrwerk.service.SensorSimulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)  // ✅ Nutzt die moderne Mockito-Extension
class SensorControllerTest {

    private MockMvc mockMvc;  // ✅ Simuliert HTTP-Requests an die API

    @Mock  // ✅ Erstellt ein Mock-Objekt für SensorPruefService
    private SensorPruefService sensorPruefService;

    @Mock  // ✅ Erstellt ein Mock-Objekt für SensorSimulator
    private SensorSimulator sensorSimulator;

    @InjectMocks  // ✅ Integriert die Mock-Services in den Controller
    private SensorController sensorController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sensorController).build();  // ✅ Initialisiert MockMvc manuell
    }

    @Test
    void testGetAktuelleSensordaten() throws Exception {
        mockMvc.perform(get("/api/sensoren/aktuelle-daten"))
                .andExpect(status().isOk());  // ✅ Erwartet HTTP 200 OK
    }

    @Test
    void testGetPruefmodus() throws Exception {
        when(sensorPruefService.getAktuellerPruefmodus()).thenReturn("SportPruefung");

        mockMvc.perform(get("/api/sensoren/pruefmodus"))
                .andExpect(status().isOk())  // ✅ Erwartet HTTP 200 OK
                .andExpect(jsonPath("$").value("Aktueller Prüfmodus: SportPruefung"));
    }

    @Test
    void testSetPruefmodus() throws Exception {
        mockMvc.perform(get("/api/sensoren/pruefmodus/sportPruefung"))
                .andExpect(status().isOk());  // ✅ Erwartet HTTP 200 OK
    }

    @Test
    void testGetWertebereich() throws Exception {
        when(sensorPruefService.getMinWert()).thenReturn(50.0);
        when(sensorPruefService.getMaxWert()).thenReturn(55.0);

        mockMvc.perform(get("/api/sensoren/wertebereich"))
                .andExpect(status().isOk())  // ✅ Erwartet HTTP 200 OK
                .andExpect(jsonPath("$").value("Aktueller Wertebereich: [50.0 - 55.0]"));
    }
}

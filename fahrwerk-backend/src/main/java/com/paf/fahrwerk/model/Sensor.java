package com.paf.fahrwerk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position; // "Vorne Links", "Hinten Rechts"
    private double hoehe;    // aktuelle Höhe des Sensors in mm
    private double minWert;  // Untere Grenze für zulässige Höhe
    private double maxWert;  // Obere Grenze für zulässige Höhe

    // Standard-Konstruktor (erforderlich für JPA)
    public Sensor() {
    }

    // Neuer Konstruktor für die Simulation
    public Sensor(Long id, String position, double hoehe) {
        this.id = id;
        this.position = position;
        this.hoehe = hoehe;
    }
}

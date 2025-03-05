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
    private double hoehe;    // aktuelle Höhe in mm
}
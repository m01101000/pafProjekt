package com.paf.fahrwerk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SensorFehler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sensorId;
    private String position;
    private double gemesseneHoehe;
    private double minWert;
    private double maxWert;
    private LocalDateTime zeitstempel = LocalDateTime.now();
}

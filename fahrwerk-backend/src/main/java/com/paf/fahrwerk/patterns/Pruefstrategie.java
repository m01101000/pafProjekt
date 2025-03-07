package com.paf.fahrwerk.patterns;

import com.paf.fahrwerk.model.Sensor;

public interface Pruefstrategie {
    boolean pruefeSensorwert(Sensor sensor);
    double getMinWert();
    double getMaxWert();
    
    // Methode zur Aktualisierung der Sensoren
    default void aktualisiereSensorWerte(Sensor sensor) {
        sensor.setMinWert(getMinWert());
        sensor.setMaxWert(getMaxWert());
    }
}

package com.paf.fahrwerk.patterns;

import com.paf.fahrwerk.model.Sensor;
import org.springframework.stereotype.Component;

@Component("ecoPruefung")
public class EcoPruefung implements Pruefstrategie {
    
    @Override
    public boolean pruefeSensorwert(Sensor sensor) {
        return sensor.getHoehe() >= getMinWert() && sensor.getHoehe() <= getMaxWert();
    }

    @Override
    public double getMinWert() {
        return 40.0;
    }

    @Override
    public double getMaxWert() {
        return 60.0;
    }
}

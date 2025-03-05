package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class SensorSimulator {
    private final SensorWebSocketService webSocketService;
    private final Random random = new Random();

    public SensorSimulator(SensorWebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Scheduled(fixedRate = 1000)
    public void sendRandomSensorData() {
        Sensor sensor = new Sensor();
        sensor.setPosition("Vorne Links");
        sensor.setHoehe(50 + random.nextDouble() * 20);
        webSocketService.sendSensorData(sensor);
    }
}
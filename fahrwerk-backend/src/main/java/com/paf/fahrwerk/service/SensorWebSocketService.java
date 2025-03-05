package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SensorWebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public SensorWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSensorData(Sensor sensor) {
        messagingTemplate.convertAndSend("/topic/sensor", sensor);
    }
}
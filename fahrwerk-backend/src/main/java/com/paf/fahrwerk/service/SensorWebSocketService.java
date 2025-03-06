package com.paf.fahrwerk.service;

import com.paf.fahrwerk.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class SensorWebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(SensorWebSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public SensorWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendSensorData(Sensor sensor) {
        logger.info("📤 Sende Sensordaten über WebSocket: Position={}, Höhe={}", sensor.getPosition(), sensor.getHoehe());
        messagingTemplate.convertAndSend("/topic/sensor", sensor);
    }
}

import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SensorService {
  private stompClient!: Client;
  public sensorData$: BehaviorSubject<any> = new BehaviorSubject(null);

  constructor() {
    this.connectWebSocket();
  }

  private connectWebSocket() {
    const socket = new SockJS('http://localhost:8080/sensor-data');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
    });

    this.stompClient.onConnect = () => {
      console.log('WebSocket verbunden!');
      this.stompClient.subscribe('/topic/sensor', (message) => {
        this.sensorData$.next(JSON.parse(message.body));
      });
    };

    this.stompClient.activate();
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { SensorService, Sensor } from '../../services/sensor.service';

@Component({
  selector: 'app-sensor',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './sensor.component.html',
  styleUrls: ['./sensor.component.css']
})
export class SensorComponent implements OnInit {

  sensoren: Sensor[] = [];
  aktuellerPruefmodus = '';

  constructor(private sensorService: SensorService) {}

  ngOnInit() {
    this.ladeSensorDaten();
    setInterval(() => this.ladeSensorDaten(), 500);
  }

  ladeSensorDaten() {
    this.sensorService.getSensorDaten().subscribe(data => this.sensoren = data);
    this.sensorService.getAktuellerPruefmodus().subscribe(modus => this.aktuellerPruefmodus = modus);
  }

  setzePruefmodus(modus: string) {
    this.sensorService.setPruefmodus(modus).subscribe(() => this.ladeSensorDaten());
  }
}

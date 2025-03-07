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
    this.sensorService.setPruefmodus(modus).subscribe(() => {
      this.aktuellerPruefmodus = modus;
      this.ladeSensorDaten();
    });
  }

  istAktiverModus(modus: string): boolean {
    return this.aktuellerPruefmodus.toLowerCase().includes(modus.toLowerCase());
  }

  getSensorByPosition(position: string): Sensor | undefined {
    return this.sensoren.find(sensor => sensor.position === position);
  }
  
  berechneProzent(sensor: Sensor): number {
    const diff = sensor.maxWert - sensor.minWert;
    if (diff === 0) return 0;
    let prozent = ((sensor.hoehe - sensor.minWert) / diff) * 100;
    prozent = Math.min(Math.max(prozent, 0), 100);
    return Math.round(prozent * 10) / 10;
  }
  
  getSensorById(id: number): Sensor | undefined {
    return this.sensoren.find(sensor => sensor.id === id);
  }

  getGradientStyle(sensor: Sensor): object {
    const percent = this.berechneProzent(sensor);
    return {
      'background': `linear-gradient(to top,#4caf4f36 ${percent}%, #ddd ${percent}%)`
    };
  }
  
  
}

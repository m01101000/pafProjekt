import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { SensorService, Sensor, SensorFehler } from '../../services/sensor.service';

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

  fehlerListe: SensorFehler[] = [];

  constructor(private sensorService: SensorService) {}

  ngOnInit() {
    this.ladeSensorDaten();
    setInterval(() => this.ladeSensorDaten(), 500);
    this.ladeFehlerDaten();
    setInterval(() => this.ladeFehlerDaten(), 500);
    this.ladeFehlermeldungen();
    setInterval(() => this.ladeFehlermeldungen(), 500); // Aktualisierung alle 5 Sekunden
  }

  ladeSensorDaten() {
    this.sensorService.getSensorDaten().subscribe(data => this.sensoren = data);
    this.sensorService.getAktuellerPruefmodus().subscribe(modus => this.aktuellerPruefmodus = modus);
  }

  ladeFehlerDaten() {
    this.sensorService.getFehlerListe().subscribe(data => this.fehlerListe = data);
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
  
  loescheAlleFehler() {
    this.sensorService.deleteAlleFehler().subscribe(response => {
      console.log(response);
      this.fehlerListe = []; // Frontend-Daten sofort leeren
    });
  }
  
  haeufigeFehler: string[] = [];
  korrelationsWarnung: string = '';

  ladeFehlermeldungen() {
    this.sensorService.getHaeufigeFehler().subscribe(data => this.haeufigeFehler = data);
    this.sensorService.getFehlerKorrelation().subscribe(warnung => this.korrelationsWarnung = warnung);
  }
  
}

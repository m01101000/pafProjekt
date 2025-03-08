import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Sensor {
  id: number;
  position: string;
  hoehe: number;
  minWert: number;
  maxWert: number;
}

export interface SensorFehler {
  id: number;
  sensorId: number;
  position: string;
  gemesseneHoehe: number;
  minWert: number;
  maxWert: number;
  zeitstempel: string;
}

@Injectable({
  providedIn: 'root'
})
export class SensorService {

  apiUrl = 'http://localhost:8080/api/sensoren';

  constructor(private http: HttpClient) { }

  getSensorDaten(): Observable<Sensor[]> {
    return this.http.get<Sensor[]>(`${this.apiUrl}/aktuelle-daten`);
  }

  getAktuellerPruefmodus(): Observable<string> {
    return this.http.get(`${this.apiUrl}/pruefmodus`, { responseType: 'text' });
  }

  setPruefmodus(modus: string): Observable<string> {
    return this.http.get(`${this.apiUrl}/pruefmodus/${modus}`, { responseType: 'text' });
  }

  getFehlerListe(): Observable<SensorFehler[]> {
    return this.http.get<SensorFehler[]>(`${this.apiUrl}/fehler`);
  }

  deleteAlleFehler(): Observable<string> {
    return this.http.delete(`${this.apiUrl}/fehler`, { responseType: 'text' });
  }
  
}

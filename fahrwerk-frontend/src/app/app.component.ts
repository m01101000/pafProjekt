// app.component.ts
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SensorComponent } from './components/sensor/sensor.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SensorComponent],  // <- Hier importierst du die Komponente explizit!
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent { }

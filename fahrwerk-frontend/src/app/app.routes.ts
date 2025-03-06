import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home.component';
import { SensorComponent } from './pages/sensor.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Startseite 🏠
  { path: 'sensor', component: SensorComponent }, // Sensordaten 📡
];

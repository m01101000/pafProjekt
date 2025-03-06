import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router'; // ✅ RouterOutlet direkt importieren

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [CommonModule, RouterOutlet, RouterLink], // ✅ KEIN `RouterModule.forRoot(routes)` hier!
})
export class AppComponent {
  title: string = 'Meine Angular App';
}

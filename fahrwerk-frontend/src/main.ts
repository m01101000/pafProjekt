import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';

function loadConfig(http: HttpClient) {
  return () => http.get('/assets/config.json').toPromise().then(config => {
    (window as any).config = config;
  });
}

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(HttpClientModule),
    {
      provide: 'APP_INITIALIZER',
      useFactory: loadConfig,
      deps: [HttpClient],
      multi: true
    }
  ]
});

describe('Sensor API Test mit Prüfmodus-Validierung', () => {
  it('Soll den aktiven Prüfmodus abrufen und die Sensordaten validieren', () => {
    
    // Zuerst den aktuellen Prüfmodus abrufen
    cy.request('GET', 'http://localhost:8080/api/sensoren/pruefmodus')
      .then((modusResponse) => {
        expect(modusResponse.status).to.eq(200);

        // Den Prüfmodus aus der API-Antwort extrahieren
        const modusText = modusResponse.body;
        cy.log(`Aktueller Prüfmodus: ${modusText}`);

        // Mappe den Text auf Min/Max-Werte je nach Modus
        const modusGrenzen = {
          "Aktueller Prüfmodus: StandardPruefung": { min: 50.0, max: 60.0 },
          "Aktueller Prüfmodus: SportPruefung": { min: 50.0, max: 55.0 },
          "Aktueller Prüfmodus: ComfortPruefung": { min: 40.0, max: 60.0 },
          "Aktueller Prüfmodus: OffroadPruefung": { min: 45.0, max: 60.0 }
        };

        // Falls der Modus nicht erkannt wird → Test fehlschlagen lassen
        expect(modusGrenzen).to.have.property(modusText);

        // Speichere die gültigen Min/Max-Werte für den aktiven Modus
        const { min, max } = modusGrenzen[modusText];
        cy.log(`Erwarteter Wertebereich: ${min} - ${max} mm`);

        // Jetzt die aktuellen Sensordaten abrufen
        cy.request('GET', 'http://localhost:8080/api/sensoren/aktuelle-daten')
          .then((sensorResponse) => {
            expect(sensorResponse.status).to.eq(200);
            expect(sensorResponse.body).to.be.an('array');

            // Prüfe für jeden Sensor die Werte
            sensorResponse.body.forEach((sensor) => {
              expect(sensor.hoehe).to.be.a('number');
              expect(sensor.hoehe).to.be.within(min, max);
              cy.log(`✅ Sensor ${sensor.position}: Höhe ${sensor.hoehe} mm liegt im Bereich (${min} - ${max})`);
            });

            cy.log('✅ Alle Sensordaten erfolgreich validiert. Test beendet.');
          });
      });
  });
});

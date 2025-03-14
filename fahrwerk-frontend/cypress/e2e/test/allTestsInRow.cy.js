describe('Sensor Prüfmodus Test', () => {
    const pruefModi = [
      { name: 'standardPruefung', response: 'StandardPruefung' },
      { name: 'sportPruefung', response: 'SportPruefung' },
      { name: 'comfortPruefung', response: 'ComfortPruefung' },
      { name: 'offroadPruefung', response: 'OffroadPruefung' }
    ];
  
    pruefModi.forEach((modus) => {
      it(`Soll den Prüfmodus auf "${modus.name}" setzen und überprüfen`, () => {
        // Modus setzen
        cy.request('GET', `http://localhost:8080/api/sensoren/pruefmodus/${modus.name}`)
          .then((response) => {
            expect(response.status).to.eq(200);
          });
  
        // Warte, damit der UI-Änderung sichtbar wird
        cy.wait(2000);
  
        // Modus abrufen und prüfen, ob er gesetzt wurde
        cy.request('GET', 'http://localhost:8080/api/sensoren/pruefmodus')
          .then((response) => {
            expect(response.status).to.eq(200);
            expect(response.body).to.include(`Aktueller Prüfmodus: ${modus.response}`);
  
            cy.log(`✅ Prüfmodus erfolgreich auf "${modus.response}" geändert und überprüft.`);
          });
  
        // Zusätzliche Wartezeit, um den Modus-Wechsel visuell nachzuvollziehen
        cy.wait(2000);
      });
    });
});
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
describe('Sensor-Fehler Löschfunktion Test', () => {
    it('Soll alle Sensor-Fehler löschen und sicherstellen, dass keine Fehler mehr existieren', () => {
      
      // 1️⃣ **Vorher: Fehler abrufen**
      cy.request('GET', 'http://localhost:8080/api/sensoren/fehler')
        .then((response) => {
          expect(response.status).to.eq(200);
          expect(response.body).to.be.an('array');
  
          if (response.body.length === 0) {
            cy.log('✅ Es gibt keine Fehler zum Löschen. Test beendet.');
            return;
          }
  
          cy.log(`⚠️ ${response.body.length} Fehler gefunden. Lösche nun...`);
  
          // 2️⃣ **Löschvorgang ausführen**
          cy.request('DELETE', 'http://localhost:8080/api/sensoren/fehler')
            .then((deleteResponse) => {
              expect(deleteResponse.status).to.eq(200);
  
              // 3️⃣ **Nachher: Prüfen, ob die Fehler wirklich weg sind**
              cy.request('GET', 'http://localhost:8080/api/sensoren/fehler')
                .then((afterDeleteResponse) => {
                  expect(afterDeleteResponse.status).to.eq(200);
                  expect(afterDeleteResponse.body).to.be.an('array');
                  expect(afterDeleteResponse.body.length).to.eq(0);
  
                  cy.log('✅ Alle Sensor-Fehler wurden erfolgreich gelöscht.');
                });
            });
        });
    });
});
describe('Überprüfung auf keine häufigen Fehler', () => {
    it('Soll sicherstellen, dass keine häufigen Fehler erkannt wurden', () => {
        // 1️⃣ **Häufige Fehler abrufen**
        cy.request('GET', 'http://localhost:8080/api/sensoren/fehler/haeufige')
            .then((response) => {
                // 2️⃣ **Prüfen, ob der Statuscode 200 ist**
                expect(response.status).to.eq(200);
                
                // 3️⃣ **Behandle die Antwort immer als Array**
                const frequentErrorsArray = Array.isArray(response.body) 
                    ? response.body 
                    : [response.body];

                // 4️⃣ **Stellen sicher, dass es genau eine Standardmeldung gibt**
                expect(frequentErrorsArray).to.have.length(1);
                expect(frequentErrorsArray[0]).to.include('Keine häufigen Sensorfehler erkannt.');

                cy.log('✅ Es wurden keine häufigen Fehler erkannt.');
            });
    });
});
describe('Überprüfung auf kritische Warnungen', () => {
    it('Soll sicherstellen, dass kritische Warnungen vorliegen', () => {
        // 1️⃣ **Kritische Warnungen abrufen**
        cy.request('GET', 'http://localhost:8080/api/sensoren/fehler/korrelation')
            .then((response) => {
                // 2️⃣ **Prüfen, ob der Statuscode 200 ist**
                expect(response.status).to.eq(200);
                
                // 3️⃣ **Prüfen, ob die Antwort eine kritische Warnung enthält**
                expect(response.body).to.be.a('string');
  
                // 4️⃣ **Stellen sicher, dass die Antwort die Standardmeldung ist**
                expect(response.body).to.include('✅ Keine kritischen Sensorwarnungen vorhanden.');
  
                cy.log('Es wurde erkannt: ' + response.body);
            });
    });
});

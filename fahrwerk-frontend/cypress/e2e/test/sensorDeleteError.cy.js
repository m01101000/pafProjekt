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
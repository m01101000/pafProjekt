describe('Sensor Prüfmodus Test - UI-Überwachung', () => {
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

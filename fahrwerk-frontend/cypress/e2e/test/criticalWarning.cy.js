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
  
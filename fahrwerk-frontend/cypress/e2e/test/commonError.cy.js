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
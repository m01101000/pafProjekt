the entire project runs inside Docker using:
---
```sh
docker-compose up --build
```


else


start backend
---
```sh
cd fahrwerk-backend
mvn test
mvn spring-boot:run
```
---
start frontend
---
```sh
cd fahrwerk-frontend
npm install
ng serve --open
```
---
frontend tests
---
```sh
cd fahrwerk-frontend
npm install cypress --save-dev
npx cypress run --spec cypress/e2e/test/allTestsInRow.cy.js
```
to run the tests individually
```sh
npx cypress run --spec cypress/e2e/test/sensorChangeMode.cy.js
npx cypress run --spec cypress/e2e/test/sensorDeleteError.cy.js
npx cypress run --spec cypress/e2e/test/commonError.cy.js
npx cypress run --spec cypress/e2e/test/criticalWarning.cy.js
```

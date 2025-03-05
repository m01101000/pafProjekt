# 🚀 Mein Full-Stack-Projekt (Angular + Spring Boot)

Dieses Repository enthält ein **Angular-Frontend** und ein **Spring Boot-Backend**.\
Folge dieser Anleitung, um das Projekt nach dem Klonen zu starten.

---

## 🛠 Backend starten (Spring Boot)

⚙ **Voraussetzungen:**

- Java 17 oder höher
- Maven installiert

### **Schritte zum Starten des Backends:**

```sh
cd fahrwerk-backend
mvn spring-boot:run
```

Das Backend läuft dann unter: [**http://localhost:8080**](http://localhost:8080)

---

## 🌐 Frontend starten (Angular)

⚙ **Voraussetzungen:**

- Node.js & npm installiert
- Angular CLI installiert (`npm install -g @angular/cli`)

### **Schritte zum Starten des Frontends:**

```sh
cd fahrwerk-frontend
npm install
ng serve
```

Das Frontend läuft dann unter: [**http://localhost:4200**](http://localhost:4200)

---

## 🎯 Nützliche Befehle

**Backend neu starten**

```sh
mvn clean spring-boot:run
```

**Frontend mit Hot-Reload starten**

```sh
ng serve --open
```

**Abhängigkeiten neu installieren**

```sh
npm install
```

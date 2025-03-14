# 1. Basis-Image mit Java 17
FROM openjdk:17-jdk-slim

# 2. Arbeitsverzeichnis setzen
WORKDIR /app

# 3. Abhängigkeiten bauen und JAR-Datei kopieren
COPY target/*.jar app.jar

# 4. Port freigeben
EXPOSE 8080

# 5. Startbefehl für das Backend
CMD ["java", "-jar", "app.jar"]

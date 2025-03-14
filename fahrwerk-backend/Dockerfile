# 1. Stufe: Backend mit Maven bauen
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app

# Kopiere nur pom.xml und installiere Abhängigkeiten (Cache-Optimierung)
COPY pom.xml .
RUN mvn dependency:go-offline

# Kopiere den Rest des Codes & baue das Projekt
COPY . .
RUN mvn clean package -DskipTests

# 2. Stufe: Das eigentliche Backend-Image mit der fertigen JAR-Datei
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Kopiere die gebaute JAR-Datei aus der ersten Stufe
COPY --from=build /app/target/*.jar app.jar

# Port 8080 freigeben
EXPOSE 8080

# Startbefehl für das Backend
CMD ["java", "-jar", "app.jar"]

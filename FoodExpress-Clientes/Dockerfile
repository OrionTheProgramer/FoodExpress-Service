# 1) Stage de build con cache de dependencias
FROM maven:3.9.7-eclipse-temurin-17 AS build

WORKDIR /app

# 1.1 Copiamos sólo POMs y wrapper para descargar dependencias
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Descarga TODAS las dependencias sin compilar a
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

# 1.2 Copiamos el resto del código
COPY src ./src

# Ahora sí empaquetamos
RUN ./mvnw -q package -DskipTests

# 2) Stage runtime
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8002
ENTRYPOINT ["java","-jar","app.jar"]

# Stage 1: build
FROM maven:3.9.7-eclipse-temurin-17 AS build

WORKDIR /app

# 1)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# 2) 
COPY src ./src
RUN mvn -B package -DskipTests

# Stage 2: runtime
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que corresponda a este micro-servicio
ARG SERVICE_PORT=8005
EXPOSE ${SERVICE_PORT}

# Perfil Spring
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java","-jar","app.jar"]

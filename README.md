# FoodExpress-Service

FoodExpress-Service es un microservicio desarrollado en Java con Spring Boot, diseñado para gestionar menús, platos y productos en una plataforma de pedidos de comida. El servicio se integra con otros microservicios para obtener información de productos y comentarios, permitiendo la generación dinámica de menús y el cálculo de ratings promedio.

## Tecnologías Utilizadas

- **Java 17**  
  Lenguaje principal de desarrollo.

- **Spring Boot 3.x**  
  Framework para el desarrollo de aplicaciones empresariales y microservicios.

- **Spring Web**  
  Para la creación de APIs RESTful.

- **Spring Data JPA**  
  Acceso y persistencia de datos con bases de datos relacionales.

- **Maven**  
  Herramienta de gestión de dependencias y construcción del proyecto.

- **PostgreSQL**  
  Base de datos relacional utilizada para almacenar la información de menús y platos.

- **Lombok**  
  Reducción de código boilerplate en entidades y servicios.

- **WebClient (Spring)**  
  Cliente HTTP reactivo para la comunicación con otros microservicios.

## Diagrama de Arquitectura

```mermaid
graph TD
    A[Cliente] -->|HTTP| B[FoodExpress-Service]
    B -->|REST API| C[Microservicio Productos]
    B -->|REST API| D[Microservicio Comentarios]
    B -->|JPA| E[(Base de Datos PostgreSQL)]
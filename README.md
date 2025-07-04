# FoodExpress-Service

FoodExpress-Service es un microservicio desarrollado en Java con Spring Boot, encargado de la gestión de menús, platos y productos dentro de la plataforma FoodExpress. Este servicio se integra con otros microservicios para obtener información de productos y comentarios, permitiendo la generación dinámica de menús y el cálculo de ratings promedio de los platos.

## Características principales

- Gestión de menús y platos (creación, actualización, eliminación y consulta).
- Integración con microservicios de productos y comentarios mediante APIs REST.
- Cálculo de ratings promedio de platos basado en comentarios de clientes.
- Persistencia de datos en base de datos relacional PostgreSQL.
- Arquitectura basada en microservicios para escalabilidad y mantenibilidad.

## Tecnologías utilizadas

- **Java 17**: Lenguaje de programación principal utilizado para implementar la lógica de negocio de todos los módulos del sistema.
- **Spring Boot 3.x**: Framework que facilita la creación de microservicios, gestionando la configuración y el ciclo de vida de la aplicación.
- **Spring Web**: Permite construir APIs RESTful para la comunicación entre módulos como Pedidos, Comentarios y Productos.
- **Spring Data JPA**: Proporciona una capa de abstracción para el acceso y la persistencia de datos en bases de datos relacionales, utilizada en la gestión de entidades como menús, platos y pedidos.
- **Maven**: Herramienta para la gestión de dependencias, construcción y empaquetado del proyecto.
- **MySQL**: Sistema de gestión de bases de datos relacional donde se almacenan los datos de los distintos módulos (por ejemplo, pedidos, menús, comentarios).
- **Lombok**: Biblioteca que reduce el código repetitivo generando automáticamente métodos como getters, setters y constructores en las entidades.
- **WebClient (Spring)**: Cliente HTTP utilizado para consumir servicios REST de otros microservicios, facilitando la integración entre módulos como Productos y Comentarios.
- **Docker**: Permite la contenerización y despliegue consistente de los microservicios en diferentes entornos.
- **Swagger/OpenAPI**: Herramienta para la documentación interactiva de las APIs REST, facilitando el desarrollo y la integración entre módulos.
- **JUnit y Mockito**: Frameworks para pruebas unitarias y de integración, asegurando la calidad del código y el correcto funcionamiento de los microservicios.

## Estructura del proyecto
El proyecto está organizado en módulos que representan las diferentes entidades y funcionalidades del sistema:
- **foodexpress-service**: Módulo principal que contiene la lógica de negocio para la gestión de menús y platos.
- **foodexpress-service-api**: Módulo que define las interfaces y contratos de las APIs REST utilizadas por otros microservicios.
- **foodexpress-service-impl**: Implementación de las APIs REST, donde se define la lógica de negocio y se gestionan las interacciones con otros microservicios.
- **foodexpress-service-model**: Contiene las entidades y modelos de datos utilizados en el servicio, como Menú, Plato y Producto.
- **foodexpress-service-repository**: Módulo que gestiona la persistencia de datos utilizando Spring Data JPA, interactuando con la base de datos MySQL.

## Instalación y ejecución
Para ejecutar el microservicio FoodExpress-Service, sigue estos pasos:
1. **Clonar el repositorio**:
   ```bash
   git clone


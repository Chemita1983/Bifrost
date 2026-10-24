# Changelog

Todos los cambios relevantes de Bifröst se documentarán en este archivo.

## [0.2.0-SNAPSHOT] - 2026-10-24

### Cambiado

- Actualizado Spring Boot de 2.2.4 a 3.5.16.
- Actualizado el proyecto para utilizar Java 17.
- Migradas las APIs de `javax` a `jakarta`.
- Migrado el test de contexto a JUnit 5.
- Añadido `spring-boot-starter-validation`.

## [0.1.0-SNAPSHOT] - 2026-10-24

### Añadido

- Configuración inicial de MariaDB mediante Docker Compose.
- Volumen persistente `bifrost_videogames_data` para conservar los datos.
- Configuración de conexión de Spring Boot mediante variables de entorno.
- Driver de MariaDB para Spring Data JPA.

### Cambiado

- Actualizada la versión de Lombok para compatibilidad con Java 17.
- Corregida la configuración del test de contexto de Spring Boot.

# Changelog

Todos los cambios relevantes de Bifröst se documentarán en este archivo.

## [0.1.0-SNAPSHOT] - 2026-10-24

### Añadido

- Configuración inicial de MariaDB mediante Docker Compose.
- Volumen persistente `bifrost_videogames_data` para conservar los datos.
- Configuración de conexión de Spring Boot mediante variables de entorno.
- Driver de MariaDB para Spring Data JPA.

### Cambiado

- Actualizada la versión de Lombok para compatibilidad con Java 17.
- Corregida la configuración del test de contexto de Spring Boot.

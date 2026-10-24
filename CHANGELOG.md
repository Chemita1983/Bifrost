# Changelog

Todos los cambios relevantes de Bifröst se documentarán en este archivo.

## [0.3.0-SNAPSHOT] - 2026-10-24

### Cambiado

- Reorganizado el proyecto siguiendo arquitectura hexagonal.
- Separado el dominio de las entidades JPA.
- Añadidos puertos de entrada y salida para videojuegos, plataformas y tiendas digitales.
- Añadidos adaptadores de entrada web y salida de persistencia.
- Añadidos DTOs y mappers para las capas web y de persistencia.
- Eliminada la estructura antigua basada en `BifrostService` y servicios genéricos.
- Corregida la nomenclatura de paquetes a `com.bifrost`.
- Añadidos endpoints para plataformas digitales.
- Añadidos tests unitarios de controladores, servicios, mappers y persistencia.

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

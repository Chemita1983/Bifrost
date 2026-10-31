# Changelog


All notable changes to Bifröst will be documented in this file.

## [Unreleased]

### Fixed

- Return HTTP 400 for invalid path or query parameter types instead of HTTP 500.

### Added

- MariaDB 11.4 Testcontainers integration tests covering API operations, filters,
  hours played and foreign-key constraints using disposable data.
- Optional nullable `hoursPlayed` for video games, validated as a non-negative integer and preserved when omitted from PATCH.
- One-time MariaDB migration to remove the legacy `players` column and align identifier sequences with existing data.
- Combinable platform, digital, completion, multiplayer, and partial case-insensitive type filters for `GET /videogames`.
- Jakarta Bean Validation for create requests and supplied PATCH fields, with centralized API error responses.
- Set English as the default locale for validation messages.
- PATCH endpoints for partially updating video games, platforms, and digital stores.
- Validation of update fields and unit tests for the controllers.
- DELETE endpoints for video games, platforms, and digital stores; associated platforms and stores cannot be deleted.

### Changed

- Return a generic message for unexpected server errors while preserving Spring HTTP error statuses and headers.
- Require existing videogame platforms and digital platforms for digital games on create and merged updates; reject invalid relations with HTTP 400.

- The Spring Boot test uses a disposable database with `create-drop` rather than
  the application's configured datasource; Docker is required for the full suite.
- Platform and digital store identifiers are included in API responses to support updates.

## [0.3.0-SNAPSHOT] - 2026-10-24

### Changed

- Reorganized the project using hexagonal architecture.
- Separated the domain from JPA entities.
- Added input and output ports for video games, platforms, and digital stores.
- Added web input and persistence output adapters.
- Added DTOs and mappers for the web and persistence layers.
- Removed the old structure based on `BifrostService` and generic services.
- Corrected package naming to `com.bifrost`.
- Added endpoints for digital platforms.
- Added unit tests for controllers, services, mappers, and persistence adapters.

## [0.2.0-SNAPSHOT] - 2026-10-24

### Changed

- Upgraded Spring Boot from 2.2.4 to 3.5.16.
- Updated the project to use Java 17.
- Migrated APIs from `javax` to `jakarta`.
- Migrated the context test to JUnit 5.

## [0.1.0-SNAPSHOT] - 2026-10-24

### Added

- Initial MariaDB configuration using Docker Compose.
- Persistent `bifrost_videogames_data` volume to preserve data.
- Spring Boot connection configuration through environment variables.
- MariaDB driver for Spring Data JPA.

### Changed

- Updated Lombok for Java 17 compatibility.
- Fixed the Spring Boot context test configuration.

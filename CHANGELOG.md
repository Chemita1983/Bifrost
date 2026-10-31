# Changelog

All notable changes to Bifröst will be documented in this file.

## [Unreleased]

### Added

- Jakarta Bean Validation for create requests and supplied PATCH fields, with centralized API error responses.
- Set English as the default locale for validation messages.
- PATCH endpoints for partially updating video games, platforms, and digital stores.
- Validation of update fields and unit tests for the controllers.
- DELETE endpoints for video games, platforms, and digital stores; associated platforms and stores cannot be deleted.

### Changed

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

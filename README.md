<div align="center">

<pre align="center">
   ██████╗  ██╗ ███████╗  ██████╗   ██████╗  ███████╗  ████████╗
   ██╔══██╗ ██║ ██╔════╝  ██╔══██╗ ██╔═══██╗ ██╔════╝  ╚══██╔══╝
██████╔╝ ██║ █████╗    ██████╔╝ ██║   ██║ ███████╗     ██║
██╔══██╗ ██║ ██╔══╝    ██╔══██╗ ██║   ██║ ╚════██║     ██║
██████╔╝ ██║ ██║       ██║  ██║ ╚██████╔╝ ███████║     ██║
╚═════╝  ╚═╝ ╚═╝       ╚═╝  ╚═╝  ╚═════╝  ╚══════╝     ╚═╝
</pre>

<strong>Your video game collection across every world</strong>

</div>

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.16-6DB33F?logo=springboot&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-Database-003545?logo=mariadb&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

<p align="center">
  <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/steam.svg" alt="Steam" height="64">
  &nbsp;&nbsp;&nbsp;
  
  <img src="https://raw.githubusercontent.com/simple-icons/simple-icons/develop/icons/playstation.svg" alt="PlayStation" height="64">
  &nbsp;&nbsp;&nbsp;
  
  <img src="https://thumb.wikimedia.org/wikipedia/commons/thumb/0/0d/Nintendo.svg/960px-Nintendo.svg.png?utm_source=commons.wikimedia.org&amp;utm_campaign=readme&amp;utm_content=thumbnail" alt="Nintendo" height="64">
</p>

## What is Bifröst?

Bifröst is an application for managing a personal video game collection,
including platforms, digital stores, and progress status.

It brings physical and digital video games from platforms such as PlayStation,
Nintendo, Xbox, PC, and Steam together in one place.

## Project goal

The final goal is to build a complete and easy-to-use catalog that makes it
possible to know:

- Which video games are part of the collection.
- Which platform each game belongs to.
- Whether a copy is physical or digital.
- Which games have been completed.
- Which games have their platinum trophy unlocked.
- Which titles are exclusively multiplayer.
- Which games are waiting to be started or completed.

The application will evolve into a complete API for managing the collection
and may later include a web interface for displaying it in a clear and
attractive way.

## Current features

- Video game management.
- Platform management.
- Digital store management.
- Video game search by name.
- Completion and platinum status tracking.
- Multiplayer indicator.
- Physical and digital copy tracking.
- MariaDB persistence.
- REST API built with Spring Boot.

## Architecture

The project is built with **hexagonal architecture** (also known as Ports and
Adapters) to keep the business logic independent from frameworks, databases,
and delivery mechanisms.

### Project structure

```text
com.bifrost
├── domain
│   └── Business models and domain rules
├── application
│   ├── port/in   Input ports and use cases
│   ├── port/out  Output ports for external dependencies
│   └── service   Application services
└── infrastructure
    ├── web               REST controllers and API DTOs
    ├── mapper            Domain, web, and persistence mappers
    └── persistence       JPA entities, repositories, and adapters
```

### Request flow

```text
HTTP request
    ↓
REST controller
    ↓
Input port / use case
    ↓
Application service
    ↓
Output port
    ↓
Persistence adapter → Spring Data JPA → MariaDB
```

The domain does not depend on Spring, JPA, MariaDB, or the REST layer. This
structure makes it possible to replace the database, API, or any adapter
without coupling them directly to the business domain.

## Technologies

- Java 17
- Spring Boot 3.5.16
- Spring Web MVC for the REST API
- Spring Data JPA
- Hibernate ORM
- MariaDB
- Docker and Docker Compose
- Maven
- JUnit 5
- Mockito

### Persistence

MariaDB runs through Docker Compose. Spring Data JPA repositories are kept in
the infrastructure layer, while application services depend on output ports
instead of depending directly on JPA repositories.

### API

The REST layer uses request and response DTOs. Mappers translate between the
external API representation and the internal domain model, preventing web
contracts from leaking into the business layer.

Existing records can be partially updated with `PATCH /videogame/{id}`,
`PATCH /platform/{id}`, and `PATCH /digital-platform/{id}`, using the same
field names as POST. Omitted fields are preserved. An unknown record or related
platform ID returns 404; PATCH never creates a record.

Only `platformId` and `digitalPlatformId` may be explicitly `null`, which
clears that relationship. Other fields reject null. Text values must be strings,
status fields must be integers 0 or 1, and `multiplayer` must be a boolean.
Unknown fields and invalid types are rejected with 400 before modifying data.
POST behavior is unchanged.

Records can be deleted using `DELETE /videogame/{id}`, `DELETE /platform/{id}`,
and `DELETE /digital-platform/{id}`. Successful deletion returns 204 and an
unknown ID returns 404. A platform or digital store referenced by a video game
cannot be deleted and returns 409; existing games are preserved.

### Testing

The project includes unit tests for application services, controllers, mappers,
and persistence adapters, as well as a Spring Boot context test.

## Getting started

### Requirements

- Java 17 or later.
- Maven.
- Docker and Docker Compose.

### Start MariaDB

```bash
docker compose up -d
```

### Run the tests

```bash
mvn clean test
```

The Spring Boot context test uses the configured MariaDB datasource and the
application's `ddl-auto: update` setting. Do not run it against a database
whose schema or data must be preserved. To run only isolated unit and web tests:

```bash
mvn -Dtest='*Test,!BifrostApplicationTests' test
```

PATCH was manually checked against a disposable MariaDB restored from a copy of
the existing schema. The schema has a `players NOT NULL` column that is not
mapped by the application, and its sequences need attention; inserts can fail.
Verify changes against a disposable copy before changing the schema or sequences.

Concurrent updates to the same record can overwrite each other. The current
implementation is intended for personal use and does not prevent this conflict.

### Run the application

```bash
mvn spring-boot:run
```

## Project status

The project is currently under active development. The API and persistence
foundation are already implemented; the next stages will focus on expanding
the use cases, improving the documentation, and creating a visual interface.

## License

This project is distributed under the MIT License.

```text
MIT License

Copyright (c) 2026 Chema

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

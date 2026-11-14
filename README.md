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

The application provides a REST API for managing the collection and can import
the Steam library. Future work includes an MCP server for compatible AI clients
(see [Project status](#project-status)).

## Current features

- Video game management.
- Platform management.
- Digital store management.
- Video game search by name.
- Completion and platinum status tracking.
- Multiplayer indicator.
- Physical and digital copy tracking.
- Steam library import and hours played synchronization.
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

`GET /videogames` lists all video games as before, or accepts any combination
of these optional query parameters (combined with AND):

- `platformId`: positive integer platform ID.
- `isDigital`, `isCompleted`, `multiplayer`: `true` or `false` (case-insensitive).
- `type`: nonblank, case-insensitive partial match on the video game type, e.g. `rpg` or `shooter`.

For example, `GET /videogames?platformId=2&isDigital=false&isCompleted=false&type=rpg`
returns matching physical, incomplete games. Incomplete means `isCompleted=0`;
it does not imply the game has not been started. No matches return `[]`.
Invalid filter values return HTTP 400. Results are not paginated.

Creating a video game requires an existing `platformId`. A digital game also
requires an existing `digitalPlatformId`; a physical game may omit it. These
requirements also apply to the resulting game after a PATCH.

Existing records can be partially updated with `PATCH /videogame/{id}`,
`PATCH /platform/{id}`, and `PATCH /digital-platform/{id}`, using the same
field names as POST. Omitted fields are preserved. An unknown record returns
404; an invalid related platform or digital store ID returns 400. PATCH never
creates a record.

The platform cannot be cleared. `digitalPlatformId` may be explicitly `null`
for a physical game, which clears the relationship. Optional `hoursPlayed` is a non-negative 32-bit integer; `null`
means unknown, while `0` is a known value. On PATCH, omitting it preserves the
current value and explicit `null` clears it. On POST, omission or `null` means
unknown. Responses omit `hoursPlayed` when unknown, consistent with other null
response fields. Other PATCH fields reject null. Text values must be strings,
status fields must be integers 0 or 1, and `multiplayer` must be a boolean.
Unknown fields and invalid types are rejected with 400 before modifying data.

For an existing schema with the legacy `players` column, review the one-time
script at `database/migrations/20261031_hours_and_sequences.sql`. It removes
`players`, adds `hours_played`, and adjusts identifier sequences. Back up the
database and stop writers before running it; it is not applied automatically.
Existing rows retain unknown hours rather than being assigned zero. The current
`ddl-auto: update` setting may also change the schema at application startup.

Records can be deleted using `DELETE /videogame/{id}`, `DELETE /platform/{id}`,
and `DELETE /digital-platform/{id}`. Successful deletion returns 204 and an
unknown ID returns 404. A platform or digital store referenced by a video game
cannot be deleted and returns 409; existing games are preserved.

### Steam synchronization

`POST /steam/sync` imports the Steam library of the configured account:

- Steam games that are not in the collection are created as digital PC games
  from the Steam store, with their name and hours played.
- Apps that the Steam store identifies as software, such as Wallpaper Engine,
  are skipped. Games, DLC, other store types and apps unknown to the store, for
  example games no longer sold, are imported. Only new apps are checked, through
  `IStoreBrowseService/GetItems`, which works in Steam's public Web API but is
  not part of its official documentation.
- Games already imported only get their `hoursPlayed` updated: the sync writes
  only that column. Name, completion, platinum, type, year, company, platforms
  and every other field entered manually are never changed.
- Nothing is ever deleted, even if a game is no longer in the Steam library.
- Games are matched by their Steam application ID, returned as `steamAppId`.
  It is set when the game is imported and cannot be changed with PATCH.
- Hours are Steam minutes divided by 60, rounded down.

`POST /steam/sync?dryRun=true` returns the same summary without saving anything.
The response is `{"created": 3, "updated": 1, "unchanged": 40, "skipped": 2, "dryRun": false}`.

Configure the account with environment variables; never commit their values:

| Variable | Description |
| --- | --- |
| `STEAM_API_KEY` | Steam Web API key. |
| `STEAM_ID` | SteamID64 of the account. |
| `STEAM_PLATFORM_ID` | Existing platform for imported games (default `6`, PC). |
| `STEAM_DIGITAL_PLATFORM_ID` | Existing digital store for imported games (default `1`, Steam). |

The game details of the Steam profile must be visible, otherwise Steam does not
return the library. If the configuration is missing or Steam fails, the endpoint
returns a generic HTTP 500 and the reason is written to the application log;
the API key is never included in errors or logs. Steam does not provide type,
year or company in this call, so those fields stay empty until filled manually.

The first start of this version adds the nullable `steam_app_id` column with a
unique constraint through `ddl-auto: update`; existing rows are not modified.
Back up the database before starting it.

### Testing

The project includes unit tests for application services, controllers, mappers,
and persistence adapters. Isolated MockMvc and persistence adapter tests mock
JPA. Integration tests use MockMvc, Spring Data JPA and a disposable MariaDB
11.4 Testcontainers instance to exercise requests, filters and foreign keys.

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

Docker must be running and accessible to Testcontainers; the MariaDB 11.4 and
Testcontainers helper images must be available locally or downloadable. No
Compose database or pre-existing data is required for tests. The integration
test overrides the datasource URL and credentials with the container values and
uses `ddl-auto: create-drop`. If the container cannot start, the test fails
instead of falling back to the configured application database. To run only
isolated unit and web tests without Docker:

```bash
mvn -Dtest='*Test,!BifrostApplicationTests' test
```

The integration tests create their own small fixtures; they do not exercise the
one-time schema migration or require a backup.

Concurrent updates to the same record can overwrite each other. The current
implementation is intended for personal use and does not prevent this conflict.

### Run the application

```bash
mvn spring-boot:run
```

## Project status

The project is currently under active development.

### Done

- REST API for video games, platforms and digital stores, with MariaDB
  persistence and integration tests on disposable MariaDB (Testcontainers).
- Steam library synchronization (`POST /steam/sync`): new games are created and
  existing ones only get their hours updated. The sync writes only the
  `hours_played` column, so data entered manually is never overwritten.
- The Steam library of the personal collection has been imported, and the
  company, type and year of every imported game were completed directly in the
  database, with no code involved. The year is the **first release of the title
  stored in the collection**, not the date Steam shows: *Trine* is 2009 even though
  Steam now lists it as *Trine Enchanted Edition*, while *DOOM + DOOM II* is
  2024 because that combined edition is a new release.
- Console games (for example PlayStation 4) are added by hand: there is no
  import for them.
- A local, unversioned `backup_bifrost.sql` is regenerated and restored on a
  disposable database after bulk data changes.

### Pending

- **New Steam games** are created with name and hours only. The Steam call does
  not provide type, year or company, so they must be completed manually (or
  with a later enrichment step; the Steam store details give the developer and
  genres but not a reliable original year).
- **Authentication:** the API has none, so it must only run locally.
- **MCP server** (see below).
- Delete the merged branches `feat/steam-sync` and `fix/steam-sync-hours-only`.

### MCP integration (proposed)

A small MCP server that wraps the existing REST API would let an AI client
manage the collection with plain language instead of SQL scripts, for example
"add Castlevania Requiem, PS4, digital, platinum" or "which games have I not
completed?". It makes sense because it reuses the API and its validation, and it
is far cheaper than writing and checking SQL for every change. Suggested scope:

- Read tools: list and search games, filter by platform, status or type.
- Write tools: create a game, patch a game (completion, platinum, hours) and
  trigger `POST /steam/sync` (with `dryRun`).
- **No delete tool**, and local use only (stdio transport) until the API has
  authentication.

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

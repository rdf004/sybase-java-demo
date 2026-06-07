# Trade Ledger Service

REST API for Fixed Income trade operations.
Spring Boot 3 application connecting to
Azure SQL.

## Stack

- **Java 21** / **Spring Boot 3.3**
- **Azure SQL** via `mssql-jdbc` + HikariCP
- **Spring Data JPA** for reference data
- **JdbcTemplate** for stored procedure calls
- **SLF4J + Logback** for logging
- **Lombok** to reduce boilerplate
- **Docker** multi-stage build

## Prerequisites

- Java 21+
- Maven 3.9+
- Azure SQL database with schema deployed

## Configuration

Set the following environment variables:

| Variable      | Description              |
|---------------|--------------------------|
| `DB_HOST`     | Azure SQL server host    |
| `DB_NAME`     | Database name            |
| `DB_USERNAME` | Database username        |
| `DB_PASSWORD` | Database password        |

## Build & Run

```bash
# Build
mvn clean package -DskipTests

# Run
DB_HOST=your-server.database.windows.net \
DB_NAME=azure_sql_demo_target \
DB_USERNAME=user \
DB_PASSWORD=pass \
java -jar target/trade-ledger-service.jar
```

## Docker

```bash
docker compose up --build
```

Pass credentials via `.env` file or shell
environment variables.

## API Endpoints

| Method | Path                       |
|--------|----------------------------|
| GET    | `/api/health`              |
| GET    | `/api/trades`              |
| GET    | `/api/trades/search`       |
| GET    | `/api/trades/{id}`         |
| POST   | `/api/trades`              |
| POST   | `/api/trades/{id}/match`   |
| POST   | `/api/trades/{id}/settle`  |
| POST   | `/api/trades/{id}/retry`   |
| GET    | `/api/trades/{id}/audit`   |
| GET    | `/api/reports/pnl`         |
| GET    | `/api/reports/aging`       |
| GET    | `/api/reports/settlements` |
| GET    | `/api/counterparties`      |
| GET    | `/api/instruments`         |
| GET    | `/api/traders`             |

## Testing

```bash
DB_HOST=your-server.database.windows.net \
DB_NAME=azure_sql_demo_target \
DB_USERNAME=user \
DB_PASSWORD=pass \
mvn verify
```

Integration tests run against live Azure SQL
to verify stored procedure parity.

# Trade Ledger Service

REST API for the Fixed Income Operations desk.
Connects to Sybase ASE (`ubs_ledger` database)
for trade lifecycle management, reporting,
and reference data lookups.

## Stack

- **Java 7** (JDK 1.7)
- **Spring 3.2** (Spring MVC, Spring JDBC)
- **Jackson 1.x** for JSON serialization
- **jConnect 7** (Sybase JDBC driver)
- **Commons DBCP 1.4** connection pool
- **Log4j 1.x** logging
- **Maven 3** with WAR packaging
- **Tomcat 7** servlet container
- **Sybase ASE 16** (`ubs_ledger` database)

## Prerequisites

- JDK 1.7.0_80 or later
- Maven 3.0.5+
- Tomcat 7
- Sybase jConnect 7 JAR installed
  in local Maven repo (see below)
- Network access to Sybase ASE host
  (port 5000)

### Install jConnect to Maven

```bash
mvn install:install-file \
  -Dfile=jconn4.jar \
  -DgroupId=com.sybase \
  -DartifactId=jconnect \
  -Dversion=7.0 \
  -Dpackaging=jar
```

## Build

```bash
mvn clean package -DskipTests
```

Produces `target/trade-ledger-service.war`.

## Deploy

Drop the WAR into Tomcat's `webapps/`
directory and restart Tomcat.

## Configuration

Edit `src/main/webapp/WEB-INF/jdbc.properties`:
- `jdbc.url` - Sybase ASE JDBC URL
- `jdbc.username` / `jdbc.password`
- Connection pool settings

## API Endpoints

All endpoints are under `/api/`.

### Health
- `GET /api/health`

### Trades
- `GET /api/trades`
- `GET /api/trades/{id}`
- `POST /api/trades`
  (calls `sp_book_trade`)

### Trade Lifecycle
- `POST /api/trades/{id}/match`
  (`sp_match_trade`)
- `POST /api/trades/{id}/settle`
  (`sp_settle_trade`)
- `POST /api/trades/{id}/retry`
  (`sp_retry_failed`)

### Reports
- `GET /api/reports/pnl`
  (`sp_daily_pnl`)
- `GET /api/reports/aging`
  (`sp_aging_report`)
- `GET /api/reports/settlements`
  (`sp_settle_report`)

### Reference Data
- `GET /api/counterparties`
- `GET /api/counterparties/{id}`
- `GET /api/instruments`
- `GET /api/instruments/{id}`
- `GET /api/traders`
- `GET /api/traders/{id}`

### Audit
- `GET /api/trades/{id}/audit`

## Database

See Python companion project for full
schema documentation.

### Stored Procedures

| Procedure | Description |
|-----------|-------------|
| `sp_book_trade` | Book new trade |
| `sp_match_trade` | Match pending trade |
| `sp_settle_trade` | Initiate settlement |
| `sp_retry_failed` | Retry failed settle |
| `sp_daily_pnl` | P&L by desk/trader |
| `sp_aging_report` | Unsettled aging |
| `sp_settle_report` | Settlement summary |

## Integration Tests

Tests run against live Sybase ASE:

```bash
mvn verify
```

Tests exercise all stored procedures and
verify end-to-end behavior. These are the
regression tests for migration parity.

## Architecture

```
Spring MVC
  Controller -> Service -> DAO
                             |
                         JdbcTemplate
                         CallableStatement
                             |
                       Sybase ASE (jConnect)
                             |
                        ubs_ledger DB
                     (tables + stored procs)
```

All configuration is XML-based:
- `web.xml` - Servlet configuration
- `applicationContext.xml` - Beans, DAOs,
  services, datasource
- `dispatcher-servlet.xml` - MVC config,
  Jackson message converter

## CI/CD

Jenkins pipeline (`Jenkinsfile`) builds
the WAR, runs integration tests, and
deploys to Tomcat via SCP/SSH on
bare-metal RHEL servers.

## Team

Platform Engineering
(`platform-eng@ubs-internal.net`)

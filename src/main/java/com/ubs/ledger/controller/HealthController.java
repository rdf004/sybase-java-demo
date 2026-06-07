package com.ubs.ledger.controller;

import java.util.Map;

import org.springframework.jdbc.core
    .JdbcTemplate;
import org.springframework.web.bind
    .annotation.GetMapping;
import org.springframework.web.bind
    .annotation.RestController;

@RestController
public class HealthController {

    private final JdbcTemplate jdbc;

    public HealthController(
        JdbcTemplate jdbc
    ) {
        this.jdbc = jdbc;
    }

    @GetMapping("/api/health")
    public Map<String, Object> health() {
        var row = jdbc.queryForMap(
            "SELECT @@SERVERNAME"
            + " AS server_name,"
            + " DB_NAME() AS db_name"
        );
        return Map.of(
            "status", "UP",
            "database", Map.of(
                "server",
                row.get("server_name"),
                "database",
                row.get("db_name")
            )
        );
    }
}

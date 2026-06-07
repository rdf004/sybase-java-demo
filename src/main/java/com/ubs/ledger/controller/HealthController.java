package com.ubs.ledger.controller;

import java.util.LinkedHashMap;
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
        try {
            var row = jdbc.queryForMap(
                "SELECT @@SERVERNAME"
                + " AS server_name,"
                + " DB_NAME() AS db_name"
            );
            var result =
                new LinkedHashMap<
                    String, Object
                >();
            result.put("status", "UP");
            result.put(
                "database", Map.of(
                    "server",
                    row.getOrDefault(
                        "server_name",
                        "unknown"
                    ),
                    "database",
                    row.getOrDefault(
                        "db_name",
                        "unknown"
                    )
                )
            );
            return result;
        } catch (Exception e) {
            var result =
                new LinkedHashMap<
                    String, Object
                >();
            result.put("status", "DOWN");
            result.put(
                "error",
                "cannot reach database"
            );
            return result;
        }
    }
}

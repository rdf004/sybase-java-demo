package com.ubs.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.jdbc
    .core.JdbcTemplate;
import org.springframework.stereotype
    .Controller;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestMethod;
import org.springframework.web.bind
    .annotation.ResponseBody;

/**
 * Health check controller.
 * Tests Sybase ASE connectivity.
 *
 * @author Platform Engineering
 * @since 1.0
 */
@Controller
public class HealthController {

    private static final Logger LOG =
        Logger.getLogger(
            HealthController.class
        );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(
        value = "/health",
        method = RequestMethod.GET
    )
    @ResponseBody
    public Map<String, Object> health() {
        Map<String, Object> result =
            new HashMap<String, Object>();
        SimpleDateFormat sdf =
            new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss"
            );
        try {
            List<Map<String, Object>> rows =
                jdbcTemplate.queryForList(
                    "SELECT @@servername"
                    + " as server_name,"
                    + " db_name()"
                    + " as db_name"
                );
            result.put("status", "UP");
            result.put(
                "database",
                rows.isEmpty()
                    ? "unknown"
                    : rows.get(0)
            );
            result.put(
                "timestamp",
                sdf.format(new Date())
            );
        } catch (Exception e) {
            LOG.error(
                "Health check failed: "
                + e.getMessage()
            );
            result.put("status", "DOWN");
            result.put(
                "error",
                "cannot reach database"
            );
        }
        return result;
    }
}

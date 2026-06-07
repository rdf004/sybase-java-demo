package com.ubs.ledger.dao;

import static org.junit.jupiter.api
    .Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.boot.test.context
    .SpringBootTest;

@SpringBootTest
class ReportDaoIntegrationTest {

    @Autowired
    private ReportDao reportDao;

    @Test
    void dailyPnl() throws Exception {
        var sdf = new SimpleDateFormat(
            "yyyy-MM-dd"
        );
        Date start =
            sdf.parse("2020-01-01");
        Date end =
            sdf.parse("2030-12-31");

        List<Map<String, Object>> result =
            reportDao.dailyPnl(start, end);
        assertNotNull(result);
        assertFalse(
            result.isEmpty(),
            "PnL should have results"
        );

        Map<String, Object> first =
            result.getFirst();
        assertTrue(
            first.containsKey("desk"),
            "Should have desk column"
        );
        assertTrue(
            first.containsKey(
                "trade_count"
            ),
            "Should have trade_count"
        );
    }

    @Test
    void agingReport() {
        List<Map<String, Object>> result =
            reportDao.agingReport();
        assertNotNull(result);
    }

    @Test
    void settlementReport() {
        List<Map<String, Object>> result =
            reportDao.settlementReport(
                null, null
            );
        assertNotNull(result);
    }
}

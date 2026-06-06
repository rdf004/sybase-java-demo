package com.ubs.ledger.dao;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.test.context
    .ContextConfiguration;
import org.springframework.test.context
    .junit4.SpringJUnit4ClassRunner;

import com.ubs.ledger.exception
    .LedgerException;

/**
 * Integration tests for ReportDao.
 * Exercises all reporting stored procs
 * against live Sybase ASE.
 *
 * @author A. Kowalski
 * @since 1.3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:test-context.xml"
})
public class ReportDaoIntegrationTest {

    @Autowired
    private ReportDao reportDao;

    @Test
    public void testDailyPnl()
        throws Exception {
        SimpleDateFormat sdf =
            new SimpleDateFormat(
                "yyyy-MM-dd"
            );
        Date start =
            sdf.parse("2020-01-01");
        Date end =
            sdf.parse("2030-12-31");

        List<Map<String, Object>> result =
            reportDao.dailyPnl(
                start, end
            );
        assertNotNull(result);
        // should have at least one row
        // from seed data
        assertFalse(
            "PnL should have results",
            result.isEmpty()
        );

        // verify expected columns
        Map<String, Object> firstRow =
            result.get(0);
        assertTrue(
            "Should have desk column",
            firstRow.containsKey("desk")
        );
        assertTrue(
            "Should have trade_count",
            firstRow.containsKey(
                "trade_count"
            )
        );
    }

    @Test
    public void testAgingReport()
        throws LedgerException {
        List<Map<String, Object>> result =
            reportDao.agingReport();
        assertNotNull(result);
        // aging report should return
        // unsettled trades from seed data
    }

    @Test
    public void testSettlementReport()
        throws LedgerException {
        List<Map<String, Object>> result =
            reportDao.settlementReport(
                null, null
            );
        assertNotNull(result);
    }
}

package com.ubs.ledger.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
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
import com.ubs.ledger.model.Trade;

/**
 * Integration tests for TradeDao.
 *
 * Runs against a live Sybase ASE instance
 * with the ubs_ledger database.
 *
 * These are the regression tests that prove
 * stored proc parity after any migration.
 *
 * Requires applicationContext.xml on
 * classpath with valid Sybase connection.
 *
 * @author A. Kowalski
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:test-context.xml"
})
public class TradeDaoIntegrationTest {

    @Autowired
    private TradeDao tradeDao;

    @Test
    public void testFindAll()
        throws LedgerException {
        List<Trade> trades =
            tradeDao.findAll(100);
        assertNotNull(trades);
        assertFalse(
            "Should have seed data",
            trades.isEmpty()
        );
        assertTrue(
            "Should have multiple trades",
            trades.size() > 1
        );
    }

    @Test
    public void testFindWithDetails()
        throws LedgerException {
        Trade trade =
            tradeDao.findWithDetails(1);
        assertNotNull(
            "Trade 1 should exist",
            trade
        );
        assertNotNull(
            "Should have cp_code",
            trade.getCpCode()
        );
        assertNotNull(
            "Should have ticker",
            trade.getTicker()
        );
        assertNotNull(
            "Should have emp_code",
            trade.getEmpCode()
        );
    }

    @Test
    public void
        testFindWithDetailsNotFound()
        throws LedgerException {
        Trade trade =
            tradeDao.findWithDetails(
                99999
            );
        assertNull(
            "Non-existent trade"
            + " should be null",
            trade
        );
    }

    @Test
    public void testSearchByStatus()
        throws LedgerException {
        Map<String, String> filters =
            new HashMap<String, String>();
        filters.put("status", "PENDING");

        List<Trade> trades =
            tradeDao.search(filters);
        assertNotNull(trades);
        for (Trade t : trades) {
            assertEquals(
                "PENDING",
                t.getStatus().trim()
            );
        }
    }

    @Test
    public void testBookTradeSp()
        throws LedgerException {
        long newId = tradeDao.bookTrade(
            "TEST-JAVA-001",
            new Date(),
            new Date(
                System.currentTimeMillis()
                + 2 * 86400000L
            ),
            1L, 1L, 1L,
            "BUY", 250.0,
            98.75, "USD", 0.0
        );
        assertTrue(
            "Should return new trade ID",
            newId > 0
        );
    }

    @Test
    public void testMatchTradeSp()
        throws LedgerException {
        // first book a trade
        long newId = tradeDao.bookTrade(
            "TEST-MATCH-001",
            new Date(),
            new Date(
                System.currentTimeMillis()
                + 2 * 86400000L
            ),
            1L, 1L, 1L,
            "SELL", 100.0,
            99.50, "USD", 0.0
        );
        assertTrue(newId > 0);

        // then match it
        tradeDao.matchTrade(
            newId, "TEST-USER"
        );

        // verify status changed
        Trade trade =
            tradeDao.findWithDetails(
                newId
            );
        assertNotNull(trade);
        assertEquals(
            "MATCHED",
            trade.getStatus().trim()
        );
    }
}

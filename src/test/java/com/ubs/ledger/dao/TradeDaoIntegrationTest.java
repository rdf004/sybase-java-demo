package com.ubs.ledger.dao;

import static org.junit.jupiter.api
    .Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.boot.test.context
    .SpringBootTest;

import com.ubs.ledger.model.Trade;

@SpringBootTest
class TradeDaoIntegrationTest {

    @Autowired
    private TradeDao tradeDao;

    @Test
    void findAll() {
        List<Trade> trades =
            tradeDao.findAll(100);
        assertNotNull(trades);
        assertFalse(
            trades.isEmpty(),
            "Should have seed data"
        );
        assertTrue(
            trades.size() > 1,
            "Should have multiple trades"
        );
    }

    @Test
    void findWithDetails() {
        Trade trade =
            tradeDao.findWithDetails(1);
        assertNotNull(
            trade,
            "Trade 1 should exist"
        );
        assertNotNull(
            trade.getCpCode(),
            "Should have cp_code"
        );
        assertNotNull(
            trade.getTicker(),
            "Should have ticker"
        );
        assertNotNull(
            trade.getEmpCode(),
            "Should have emp_code"
        );
    }

    @Test
    void findWithDetailsNotFound() {
        Trade trade =
            tradeDao.findWithDetails(
                99999
            );
        assertNull(
            trade,
            "Non-existent trade"
            + " should be null"
        );
    }

    @Test
    void searchByStatus() {
        var filters =
            new HashMap<String, String>();
        filters.put("status", "PENDING");

        List<Trade> trades =
            tradeDao.search(filters);
        assertNotNull(trades);
        for (var t : trades) {
            assertEquals(
                "PENDING",
                t.getStatus().trim()
            );
        }
    }

    @Test
    void bookTradeSp() {
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
            newId > 0,
            "Should return new trade ID"
        );
    }

    @Test
    void matchTradeSp() {
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

        tradeDao.matchTrade(
            newId, "TEST-USER"
        );

        Trade trade =
            tradeDao.findWithDetails(newId);
        assertNotNull(trade);
        assertEquals(
            "MATCHED",
            trade.getStatus().trim()
        );
    }
}

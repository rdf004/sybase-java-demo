package com.ubs.ledger.service;

import static org.junit.jupiter.api
    .Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.boot.test.context
    .SpringBootTest;

import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;

@SpringBootTest
class TradeServiceIntegrationTest {

    @Autowired
    private TradeService tradeService;

    @Test
    void listTrades() {
        List<Trade> trades =
            tradeService.listTrades(50);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
    }

    @Test
    void searchByStatus() {
        var filters =
            new HashMap<String, String>();
        filters.put("status", "SETTLED");

        List<Trade> trades =
            tradeService.searchTrades(
                filters
            );
        assertNotNull(trades);
        for (var t : trades) {
            assertEquals(
                "SETTLED",
                t.getStatus().trim()
            );
        }
    }

    @Test
    void getTradeDetail() {
        Trade trade =
            tradeService.getTradeDetail(1);
        assertNotNull(trade);
        assertNotNull(trade.getTradeRef());
        assertNotNull(trade.getCpCode());
        assertNotNull(trade.getTicker());
    }

    @Test
    void getAuditTrail() {
        List<TradeAudit> audit =
            tradeService.getAuditTrail(1);
        assertNotNull(audit);
        assertFalse(
            audit.isEmpty(),
            "Should have audit records"
        );
    }

    @Test
    void getSettlements() {
        List<Settlement> settlements =
            tradeService.getSettlements(1);
        assertNotNull(settlements);
    }

    @Test
    void fullLifecycle() {
        long tradeId =
            tradeService.bookTrade(
                "LIFECYCLE-JAVA-001",
                new Date(),
                new Date(
                    System
                    .currentTimeMillis()
                    + 2 * 86400000L
                ),
                1L, 1L, 1L,
                "BUY", 500.0,
                101.25, "USD", 0.0
            );
        assertTrue(
            tradeId > 0,
            "Should return new trade ID"
        );

        List<TradeAudit> audit =
            tradeService.getAuditTrail(
                tradeId
            );
        assertFalse(
            audit.isEmpty(),
            "Booking should create audit"
        );

        tradeService.matchTrade(
            tradeId, "TEST-MATCHER"
        );
        Trade matched =
            tradeService.getTradeDetail(
                tradeId
            );
        assertEquals(
            "MATCHED",
            matched.getStatus().trim(),
            "Should be MATCHED"
        );

        tradeService.settleTrade(
            tradeId, "DVP"
        );

        List<Settlement> settlements =
            tradeService.getSettlements(
                tradeId
            );
        assertFalse(
            settlements.isEmpty(),
            "Should have settlement"
        );
    }
}

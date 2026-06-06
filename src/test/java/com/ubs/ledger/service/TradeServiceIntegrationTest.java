package com.ubs.ledger.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
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
import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;

/**
 * Integration tests for TradeService.
 * Exercises the full stack against live
 * Sybase ASE.
 *
 * These tests verify:
 * - Trade CRUD via stored procs
 * - Trade lifecycle (book -> match ->
 *   settle)
 * - Audit trail generation
 * - Settlement record creation
 *
 * @author A. Kowalski
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:test-service-context.xml"
})
public class TradeServiceIntegrationTest {

    @Autowired
    private TradeService tradeService;

    @Test
    public void testListTrades()
        throws LedgerException {
        List<Trade> trades =
            tradeService.listTrades(50);
        assertNotNull(trades);
        assertFalse(trades.isEmpty());
    }

    @Test
    public void testSearchByStatus()
        throws LedgerException {
        Map<String, String> filters =
            new HashMap<String, String>();
        filters.put("status", "SETTLED");

        List<Trade> trades =
            tradeService.searchTrades(
                filters
            );
        assertNotNull(trades);
        for (Trade t : trades) {
            assertEquals(
                "SETTLED",
                t.getStatus().trim()
            );
        }
    }

    @Test
    public void testGetTradeDetail()
        throws LedgerException {
        Trade trade =
            tradeService.getTradeDetail(1);
        assertNotNull(trade);
        assertNotNull(trade.getTradeRef());
        assertNotNull(trade.getCpCode());
        assertNotNull(trade.getTicker());
    }

    @Test
    public void testGetAuditTrail()
        throws LedgerException {
        List<TradeAudit> audit =
            tradeService.getAuditTrail(1);
        assertNotNull(audit);
        // trade 1 from seed data should
        // have at least one audit entry
        assertFalse(
            "Should have audit records",
            audit.isEmpty()
        );
    }

    @Test
    public void testGetSettlements()
        throws LedgerException {
        // trade 1 should have settlements
        // from seed data
        List<Settlement> settlements =
            tradeService.getSettlements(1);
        assertNotNull(settlements);
    }

    /**
     * Full lifecycle test:
     * Book -> Match -> Settle
     *
     * This is the golden regression test.
     * If this passes after a migration,
     * the core workflow is intact.
     */
    @Test
    public void testFullLifecycle()
        throws LedgerException {
        // 1. Book
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
            "Should return new trade ID",
            tradeId > 0
        );

        // verify audit for booking
        List<TradeAudit> audit =
            tradeService.getAuditTrail(
                tradeId
            );
        assertFalse(
            "Booking should create audit",
            audit.isEmpty()
        );

        // 2. Match
        tradeService.matchTrade(
            tradeId, "TEST-MATCHER"
        );
        Trade matched =
            tradeService.getTradeDetail(
                tradeId
            );
        assertEquals(
            "Should be MATCHED",
            "MATCHED",
            matched.getStatus().trim()
        );

        // 3. Settle
        tradeService.settleTrade(
            tradeId, "DVP"
        );

        // verify settlement created
        List<Settlement> settlements =
            tradeService.getSettlements(
                tradeId
            );
        assertFalse(
            "Should have settlement",
            settlements.isEmpty()
        );
    }
}

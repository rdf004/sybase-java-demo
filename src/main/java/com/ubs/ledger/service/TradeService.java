package com.ubs.ledger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction
    .support.TransactionCallback;
import org.springframework.transaction
    .support.TransactionTemplate;
import org.springframework.transaction
    .TransactionStatus;

import com.ubs.ledger.dao.SettlementDao;
import com.ubs.ledger.dao.TradeAuditDao;
import com.ubs.ledger.dao.TradeDao;
import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;

/**
 * Service layer for trade operations.
 * Coordinates DAO calls and manages
 * transactions.
 *
 * Uses setter injection (wired in
 * applicationContext.xml).
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class TradeService {

    private static final Logger LOG =
        Logger.getLogger(
            TradeService.class
        );

    private TradeDao tradeDao;
    private TradeAuditDao tradeAuditDao;
    private SettlementDao settlementDao;
    private TransactionTemplate
        transactionTemplate;

    // Setter injection for XML wiring
    public void setTradeDao(
        TradeDao tradeDao
    ) {
        this.tradeDao = tradeDao;
    }

    public void setTradeAuditDao(
        TradeAuditDao tradeAuditDao
    ) {
        this.tradeAuditDao = tradeAuditDao;
    }

    public void setSettlementDao(
        SettlementDao settlementDao
    ) {
        this.settlementDao = settlementDao;
    }

    public void setTransactionTemplate(
        TransactionTemplate
            transactionTemplate
    ) {
        this.transactionTemplate =
            transactionTemplate;
    }

    /**
     * List all trades.
     */
    public List<Trade> listTrades(
        int maxRows
    ) throws LedgerException {
        return tradeDao.findAll(maxRows);
    }

    /**
     * Search trades with filters.
     */
    public List<Trade> searchTrades(
        Map<String, String> filters
    ) throws LedgerException {
        return tradeDao.search(filters);
    }

    /**
     * Get trade detail with related data.
     */
    public Trade getTradeDetail(
        long tradeId
    ) throws LedgerException {
        return tradeDao.findWithDetails(
            tradeId
        );
    }

    /**
     * Get settlements for a trade.
     */
    public List<Settlement> getSettlements(
        long tradeId
    ) throws LedgerException {
        return settlementDao.findByTradeId(
            tradeId
        );
    }

    /**
     * Get audit trail for a trade.
     */
    public List<TradeAudit> getAuditTrail(
        long tradeId
    ) throws LedgerException {
        return tradeAuditDao.findByTradeId(
            tradeId
        );
    }

    /**
     * Book a new trade via stored proc.
     *
     * The stored proc handles notional
     * calculation and audit record
     * creation internally, so we just
     * call it and return the new trade ID.
     */
    public long bookTrade(
        String tradeRef,
        Date tradeDate,
        Date settleDate,
        long traderId,
        long cpId,
        long instrId,
        String direction,
        double quantity,
        double price,
        String currency,
        double accruedInt
    ) throws LedgerException {
        LOG.info(
            "Booking trade: " + tradeRef
        );
        return tradeDao.bookTrade(
            tradeRef, tradeDate,
            settleDate, traderId,
            cpId, instrId, direction,
            quantity, price, currency,
            accruedInt
        );
    }

    /**
     * Match a trade via stored proc.
     */
    public void matchTrade(
        long tradeId, String matchedBy
    ) throws LedgerException {
        LOG.info(
            "Matching trade: " + tradeId
            + " by " + matchedBy
        );
        tradeDao.matchTrade(
            tradeId, matchedBy
        );
    }

    /**
     * Settle a trade via stored proc.
     */
    public void settleTrade(
        long tradeId, String settleMethod
    ) throws LedgerException {
        LOG.info(
            "Settling trade: " + tradeId
            + " method=" + settleMethod
        );
        tradeDao.settleTrade(
            tradeId, settleMethod
        );
    }

    /**
     * Retry a failed trade via stored
     * proc.
     */
    public void retryFailed(
        long tradeId, String settleMethod
    ) throws LedgerException {
        LOG.info(
            "Retrying trade: " + tradeId
        );
        tradeDao.retryFailed(
            tradeId, settleMethod
        );
    }
}

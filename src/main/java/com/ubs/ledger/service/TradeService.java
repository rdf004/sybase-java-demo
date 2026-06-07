package com.ubs.ledger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype
    .Service;

import com.ubs.ledger.dao.TradeDao;
import com.ubs.ledger.model.Settlement;
import com.ubs.ledger.model.Trade;
import com.ubs.ledger.model.TradeAudit;
import com.ubs.ledger.repository
    .SettlementRepository;
import com.ubs.ledger.repository
    .TradeAuditRepository;

@Service
public class TradeService {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            TradeService.class
        );

    private final TradeDao tradeDao;
    private final TradeAuditRepository
        auditRepo;
    private final SettlementRepository
        settlementRepo;

    public TradeService(
        TradeDao tradeDao,
        TradeAuditRepository auditRepo,
        SettlementRepository settlementRepo
    ) {
        this.tradeDao = tradeDao;
        this.auditRepo = auditRepo;
        this.settlementRepo =
            settlementRepo;
    }

    public List<Trade> listTrades(
        int maxRows
    ) {
        return tradeDao.findAll(maxRows);
    }

    public List<Trade> searchTrades(
        Map<String, String> filters
    ) {
        return tradeDao.search(filters);
    }

    public Trade getTradeDetail(
        long tradeId
    ) {
        return tradeDao.findWithDetails(
            tradeId
        );
    }

    public List<Settlement> getSettlements(
        long tradeId
    ) {
        return settlementRepo
            .findByTradeIdOrderByAttemptNum(
                tradeId
            );
    }

    public List<TradeAudit> getAuditTrail(
        long tradeId
    ) {
        return auditRepo
            .findByTradeIdOrderByChangedAt(
                tradeId
            );
    }

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
    ) {
        LOG.info(
            "Booking trade: {}", tradeRef
        );
        return tradeDao.bookTrade(
            tradeRef, tradeDate,
            settleDate, traderId,
            cpId, instrId, direction,
            quantity, price, currency,
            accruedInt
        );
    }

    public void matchTrade(
        long tradeId, String matchedBy
    ) {
        LOG.info(
            "Matching trade: {} by {}",
            tradeId, matchedBy
        );
        tradeDao.matchTrade(
            tradeId, matchedBy
        );
    }

    public void settleTrade(
        long tradeId, String settleMethod
    ) {
        LOG.info(
            "Settling trade: {} method={}",
            tradeId, settleMethod
        );
        tradeDao.settleTrade(
            tradeId, settleMethod
        );
    }

    public void retryFailed(
        long tradeId, String settleMethod
    ) {
        LOG.info(
            "Retrying trade: {}", tradeId
        );
        tradeDao.retryFailed(
            tradeId, settleMethod
        );
    }
}

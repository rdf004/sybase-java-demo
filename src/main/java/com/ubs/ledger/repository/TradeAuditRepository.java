package com.ubs.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository
    .JpaRepository;

import com.ubs.ledger.model.TradeAudit;

public interface TradeAuditRepository
    extends JpaRepository<TradeAudit, Long> {

    List<TradeAudit>
        findByTradeIdOrderByChangedAt(
            Long tradeId
        );
}

package com.ubs.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository
    .JpaRepository;

import com.ubs.ledger.model.Settlement;

public interface SettlementRepository
    extends JpaRepository<Settlement, Long> {

    List<Settlement>
        findByTradeIdOrderByAttemptNum(
            Long tradeId
        );
}

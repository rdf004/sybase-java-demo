package com.ubs.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository
    .JpaRepository;

import com.ubs.ledger.model.Counterparty;

public interface CounterpartyRepository
    extends JpaRepository<Counterparty, Long> {

    List<Counterparty> findAllByOrderByCpCode();
}

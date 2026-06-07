package com.ubs.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository
    .JpaRepository;

import com.ubs.ledger.model.Trader;

public interface TraderRepository
    extends JpaRepository<Trader, Long> {

    List<Trader> findAllByOrderByEmpCode();
}

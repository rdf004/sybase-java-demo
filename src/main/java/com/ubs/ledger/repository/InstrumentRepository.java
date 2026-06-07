package com.ubs.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository
    .JpaRepository;

import com.ubs.ledger.model.Instrument;

public interface InstrumentRepository
    extends JpaRepository<Instrument, Long> {

    List<Instrument> findAllByOrderByTicker();
}

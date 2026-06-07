package com.ubs.ledger.service;

import java.util.List;

import org.springframework.stereotype
    .Service;

import com.ubs.ledger.model.Counterparty;
import com.ubs.ledger.model.Instrument;
import com.ubs.ledger.model.Trader;
import com.ubs.ledger.repository
    .CounterpartyRepository;
import com.ubs.ledger.repository
    .InstrumentRepository;
import com.ubs.ledger.repository
    .TraderRepository;

@Service
public class ReferenceDataService {

    private final CounterpartyRepository
        cpRepo;
    private final InstrumentRepository
        instrRepo;
    private final TraderRepository
        traderRepo;

    public ReferenceDataService(
        CounterpartyRepository cpRepo,
        InstrumentRepository instrRepo,
        TraderRepository traderRepo
    ) {
        this.cpRepo = cpRepo;
        this.instrRepo = instrRepo;
        this.traderRepo = traderRepo;
    }

    public List<Counterparty>
        listCounterparties() {
        return cpRepo
            .findAllByOrderByCpCode();
    }

    public Counterparty getCounterparty(
        long cpId
    ) {
        return cpRepo.findById(cpId)
            .orElse(null);
    }

    public List<Instrument>
        listInstruments() {
        return instrRepo
            .findAllByOrderByTicker();
    }

    public Instrument getInstrument(
        long instrId
    ) {
        return instrRepo.findById(instrId)
            .orElse(null);
    }

    public List<Trader> listTraders() {
        return traderRepo
            .findAllByOrderByEmpCode();
    }

    public Trader getTrader(long traderId) {
        return traderRepo
            .findById(traderId)
            .orElse(null);
    }
}

package com.ubs.ledger.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.ubs.ledger.dao.CounterpartyDao;
import com.ubs.ledger.dao.InstrumentDao;
import com.ubs.ledger.dao.TraderDao;
import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Counterparty;
import com.ubs.ledger.model.Instrument;
import com.ubs.ledger.model.Trader;

/**
 * Service for reference data lookups.
 * Counterparties, instruments, traders.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class ReferenceDataService {

    private static final Logger LOG =
        Logger.getLogger(
            ReferenceDataService.class
        );

    private CounterpartyDao
        counterpartyDao;
    private InstrumentDao instrumentDao;
    private TraderDao traderDao;

    public void setCounterpartyDao(
        CounterpartyDao counterpartyDao
    ) {
        this.counterpartyDao =
            counterpartyDao;
    }

    public void setInstrumentDao(
        InstrumentDao instrumentDao
    ) {
        this.instrumentDao = instrumentDao;
    }

    public void setTraderDao(
        TraderDao traderDao
    ) {
        this.traderDao = traderDao;
    }

    // Counterparties
    public List<Counterparty>
        listCounterparties()
        throws LedgerException {
        return counterpartyDao.findAll();
    }

    public Counterparty getCounterparty(
        long cpId
    ) throws LedgerException {
        return counterpartyDao.findById(
            cpId
        );
    }

    // Instruments
    public List<Instrument>
        listInstruments()
        throws LedgerException {
        return instrumentDao.findAll();
    }

    public Instrument getInstrument(
        long instrId
    ) throws LedgerException {
        return instrumentDao.findById(
            instrId
        );
    }

    // Traders
    public List<Trader> listTraders()
        throws LedgerException {
        return traderDao.findAll();
    }

    public Trader getTrader(long traderId)
        throws LedgerException {
        return traderDao.findById(
            traderId
        );
    }
}

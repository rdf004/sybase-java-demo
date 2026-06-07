package com.ubs.ledger.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind
    .annotation.GetMapping;
import org.springframework.web.bind
    .annotation.PathVariable;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RestController;

import com.ubs.ledger.exception
    .ResourceNotFoundException;
import com.ubs.ledger.model.Counterparty;
import com.ubs.ledger.model.Instrument;
import com.ubs.ledger.model.Trader;
import com.ubs.ledger.service
    .ReferenceDataService;

@RestController
@RequestMapping("/api")
public class ReferenceDataController {

    private final ReferenceDataService svc;

    public ReferenceDataController(
        ReferenceDataService svc
    ) {
        this.svc = svc;
    }

    @GetMapping("/counterparties")
    public Map<String, Object>
        listCounterparties() {
        List<Counterparty> list =
            svc.listCounterparties();
        return Map.of(
            "counterparties", list,
            "count", list.size()
        );
    }

    @GetMapping(
        "/counterparties/{cpId}"
    )
    public Counterparty getCounterparty(
        @PathVariable long cpId
    ) {
        Counterparty cp =
            svc.getCounterparty(cpId);
        if (cp == null) {
            throw
                new ResourceNotFoundException(
                    "not found"
                );
        }
        return cp;
    }

    @GetMapping("/instruments")
    public Map<String, Object>
        listInstruments() {
        List<Instrument> list =
            svc.listInstruments();
        return Map.of(
            "instruments", list,
            "count", list.size()
        );
    }

    @GetMapping(
        "/instruments/{instrId}"
    )
    public Instrument getInstrument(
        @PathVariable long instrId
    ) {
        Instrument i =
            svc.getInstrument(instrId);
        if (i == null) {
            throw
                new ResourceNotFoundException(
                    "not found"
                );
        }
        return i;
    }

    @GetMapping("/traders")
    public Map<String, Object>
        listTraders() {
        List<Trader> list =
            svc.listTraders();
        return Map.of(
            "traders", list,
            "count", list.size()
        );
    }

    @GetMapping("/traders/{traderId}")
    public Trader getTrader(
        @PathVariable long traderId
    ) {
        Trader t =
            svc.getTrader(traderId);
        if (t == null) {
            throw
                new ResourceNotFoundException(
                    "not found"
                );
        }
        return t;
    }
}

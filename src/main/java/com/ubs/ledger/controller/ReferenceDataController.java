package com.ubs.ledger.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory
    .annotation.Autowired;
import org.springframework.http
    .HttpStatus;
import org.springframework.http
    .ResponseEntity;
import org.springframework.stereotype
    .Controller;
import org.springframework.web.bind
    .annotation.PathVariable;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestMethod;
import org.springframework.web.bind
    .annotation.ResponseBody;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Counterparty;
import com.ubs.ledger.model.Instrument;
import com.ubs.ledger.model.Trader;
import com.ubs.ledger.service
    .ReferenceDataService;

/**
 * REST controller for reference data.
 * Counterparties, instruments, traders.
 *
 * @author Platform Engineering
 * @since 1.0
 */
@Controller
public class ReferenceDataController {

    private static final Logger LOG =
        Logger.getLogger(
            ReferenceDataController.class
        );

    @Autowired
    private ReferenceDataService
        referenceDataService;

    // ===== Counterparties =====

    @RequestMapping(
        value = "/counterparties",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > listCounterparties() {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            List<Counterparty> cps =
                referenceDataService
                .listCounterparties();
            response.put(
                "counterparties", cps
            );
            response.put(
                "count", cps.size()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    @RequestMapping(
        value = "/counterparties/{cpId}",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Object>
        getCounterparty(
        @PathVariable long cpId
    ) {
        try {
            Counterparty cp =
                referenceDataService
                .getCounterparty(cpId);
            if (cp == null) {
                Map<String, Object> err =
                    new HashMap
                    <String, Object>();
                err.put(
                    "error", "not found"
                );
                return new ResponseEntity
                    <Object>(
                        err,
                        HttpStatus.NOT_FOUND
                    );
            }
            return new ResponseEntity
                <Object>(
                    cp, HttpStatus.OK
                );
        } catch (LedgerException e) {
            Map<String, Object> err =
                new HashMap
                <String, Object>();
            err.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Object>(
                    err,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    // ===== Instruments =====

    @RequestMapping(
        value = "/instruments",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > listInstruments() {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            List<Instrument> instruments =
                referenceDataService
                .listInstruments();
            response.put(
                "instruments", instruments
            );
            response.put(
                "count",
                instruments.size()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    @RequestMapping(
        value = "/instruments/{instrId}",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Object>
        getInstrument(
        @PathVariable long instrId
    ) {
        try {
            Instrument instr =
                referenceDataService
                .getInstrument(instrId);
            if (instr == null) {
                Map<String, Object> err =
                    new HashMap
                    <String, Object>();
                err.put(
                    "error", "not found"
                );
                return new ResponseEntity
                    <Object>(
                        err,
                        HttpStatus.NOT_FOUND
                    );
            }
            return new ResponseEntity
                <Object>(
                    instr, HttpStatus.OK
                );
        } catch (LedgerException e) {
            Map<String, Object> err =
                new HashMap
                <String, Object>();
            err.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Object>(
                    err,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    // ===== Traders =====

    @RequestMapping(
        value = "/traders",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > listTraders() {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            List<Trader> traders =
                referenceDataService
                .listTraders();
            response.put(
                "traders", traders
            );
            response.put(
                "count", traders.size()
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            response.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    @RequestMapping(
        value = "/traders/{traderId}",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Object>
        getTrader(
        @PathVariable long traderId
    ) {
        try {
            Trader trader =
                referenceDataService
                .getTrader(traderId);
            if (trader == null) {
                Map<String, Object> err =
                    new HashMap
                    <String, Object>();
                err.put(
                    "error", "not found"
                );
                return new ResponseEntity
                    <Object>(
                        err,
                        HttpStatus.NOT_FOUND
                    );
            }
            return new ResponseEntity
                <Object>(
                    trader, HttpStatus.OK
                );
        } catch (LedgerException e) {
            Map<String, Object> err =
                new HashMap
                <String, Object>();
            err.put(
                "error",
                "something went wrong"
            );
            return new ResponseEntity
                <Object>(
                    err,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }
}

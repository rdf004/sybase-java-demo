package com.ubs.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
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
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestMethod;
import org.springframework.web.bind
    .annotation.RequestParam;
import org.springframework.web.bind
    .annotation.ResponseBody;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.service
    .ReportService;

/**
 * REST controller for reports.
 * Calls Sybase stored procedures for
 * P&L, aging, and settlement reports.
 *
 * @author Platform Engineering
 * @since 1.3
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    private static final Logger LOG =
        Logger.getLogger(
            ReportController.class
        );

    private static final SimpleDateFormat
        DATE_FMT = new SimpleDateFormat(
            "yyyy-MM-dd"
        );

    @Autowired
    private ReportService reportService;

    /**
     * GET /api/reports/pnl
     */
    @RequestMapping(
        value = "/pnl",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > dailyPnl(
        @RequestParam(
            value = "start_date",
            required = false
        ) String startStr,
        @RequestParam(
            value = "end_date",
            required = false
        ) String endStr
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            Date startDate;
            Date endDate;
            if (startStr != null) {
                startDate =
                    DATE_FMT.parse(
                        startStr
                    );
            } else {
                startDate =
                    DATE_FMT.parse(
                        "2012-01-01"
                    );
            }
            if (endStr != null) {
                endDate =
                    DATE_FMT.parse(endStr);
            } else {
                endDate = new Date();
            }

            List<Map<String, Object>> data =
                reportService.getDailyPnl(
                    startDate, endDate
                );

            response.put(
                "report", "daily_pnl"
            );
            response.put(
                "start_date",
                DATE_FMT.format(startDate)
            );
            response.put(
                "end_date",
                DATE_FMT.format(endDate)
            );
            response.put("data", data);
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (Exception e) {
            LOG.error(
                "PnL report failed: "
                + e.getMessage()
            );
            response.put(
                "error", "report failed"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * GET /api/reports/aging
     */
    @RequestMapping(
        value = "/aging",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > agingReport() {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            List<Map<String, Object>> data =
                reportService
                    .getAgingReport();

            response.put(
                "report", "aging"
            );
            response.put("data", data);
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (LedgerException e) {
            LOG.error(
                "Aging report failed: "
                + e.getMessage()
            );
            response.put(
                "error", "report failed"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }

    /**
     * GET /api/reports/settlements
     */
    @RequestMapping(
        value = "/settlements",
        method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<
        Map<String, Object>
    > settlementReport(
        @RequestParam(
            value = "start_date",
            required = false
        ) String startStr,
        @RequestParam(
            value = "end_date",
            required = false
        ) String endStr
    ) {
        Map<String, Object> response =
            new HashMap<String, Object>();
        try {
            Date startDate = null;
            Date endDate = null;
            if (startStr != null) {
                startDate =
                    DATE_FMT.parse(
                        startStr
                    );
            }
            if (endStr != null) {
                endDate =
                    DATE_FMT.parse(endStr);
            }

            List<Map<String, Object>> data =
                reportService
                .getSettlementReport(
                    startDate, endDate
                );

            response.put(
                "report", "settlements"
            );
            response.put("data", data);
            return new ResponseEntity
                <Map<String, Object>>(
                    response, HttpStatus.OK
                );
        } catch (Exception e) {
            LOG.error(
                "Settlement report"
                + " failed: "
                + e.getMessage()
            );
            response.put(
                "error", "report failed"
            );
            return new ResponseEntity
                <Map<String, Object>>(
                    response,
                    HttpStatus
                    .INTERNAL_SERVER_ERROR
                );
        }
    }
}

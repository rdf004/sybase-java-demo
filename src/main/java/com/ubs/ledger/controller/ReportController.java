package com.ubs.ledger.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind
    .annotation.GetMapping;
import org.springframework.web.bind
    .annotation.RequestMapping;
import org.springframework.web.bind
    .annotation.RequestParam;
import org.springframework.web.bind
    .annotation.RestController;

import com.ubs.ledger.service
    .ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ReportController.class
        );

    private static final SimpleDateFormat
        DATE_FMT = new SimpleDateFormat(
            "yyyy-MM-dd"
        );

    private final ReportService svc;

    public ReportController(
        ReportService svc
    ) {
        this.svc = svc;
    }

    @GetMapping("/pnl")
    public Map<String, Object> pnl(
        @RequestParam(
            value = "start_date",
            required = false
        ) String startStr,
        @RequestParam(
            value = "end_date",
            required = false
        ) String endStr
    ) throws Exception {
        Date start = null;
        Date end = null;
        synchronized (DATE_FMT) {
            if (startStr != null) {
                start = DATE_FMT.parse(
                    startStr
                );
            }
            if (endStr != null) {
                end = DATE_FMT.parse(
                    endStr
                );
            }
        }
        if (start != null && end == null) {
            end = new Date();
        }
        if (start == null && end != null) {
            start = DATE_FMT.parse(
                "2012-01-01"
            );
        }

        List<Map<String, Object>> data =
            svc.getDailyPnl(start, end);
        return Map.of(
            "report", "daily_pnl",
            "data", data
        );
    }

    @GetMapping("/aging")
    public Map<String, Object> aging() {
        List<Map<String, Object>> data =
            svc.getAgingReport();
        return Map.of(
            "report", "aging",
            "data", data
        );
    }

    @GetMapping("/settlements")
    public Map<String, Object> settlements(
        @RequestParam(
            value = "start_date",
            required = false
        ) String startStr,
        @RequestParam(
            value = "end_date",
            required = false
        ) String endStr
    ) throws Exception {
        Date start = null;
        Date end = null;
        synchronized (DATE_FMT) {
            if (startStr != null) {
                start = DATE_FMT.parse(
                    startStr
                );
            }
            if (endStr != null) {
                end = DATE_FMT.parse(
                    endStr
                );
            }
        }
        if (start != null && end == null) {
            end = new Date();
        }
        if (start == null && end != null) {
            start = DATE_FMT.parse(
                "2012-01-01"
            );
        }

        List<Map<String, Object>> data =
            svc.getSettlementReport(
                start, end
            );
        return Map.of(
            "report", "settlements",
            "data", data
        );
    }
}

package com.ubs.ledger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype
    .Service;

import com.ubs.ledger.dao.ReportDao;

@Service
public class ReportService {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ReportService.class
        );

    private final ReportDao reportDao;

    public ReportService(
        ReportDao reportDao
    ) {
        this.reportDao = reportDao;
    }

    public List<Map<String, Object>>
        getDailyPnl(
            Date startDate, Date endDate
        ) {
        LOG.info(
            "Running daily P&L report"
        );
        return reportDao.dailyPnl(
            startDate, endDate
        );
    }

    public List<Map<String, Object>>
        getAgingReport() {
        LOG.info("Running aging report");
        return reportDao.agingReport();
    }

    public List<Map<String, Object>>
        getSettlementReport(
            Date startDate, Date endDate
        ) {
        LOG.info(
            "Running settlement report"
        );
        return reportDao.settlementReport(
            startDate, endDate
        );
    }
}

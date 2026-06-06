package com.ubs.ledger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ubs.ledger.dao.ReportDao;
import com.ubs.ledger.exception
    .LedgerException;

/**
 * Service for report generation.
 * Delegates to ReportDao which calls
 * Sybase stored procedures.
 *
 * @author Platform Engineering
 * @since 1.3
 */
public class ReportService {

    private static final Logger LOG =
        Logger.getLogger(
            ReportService.class
        );

    private ReportDao reportDao;

    public void setReportDao(
        ReportDao reportDao
    ) {
        this.reportDao = reportDao;
    }

    /**
     * Daily P&L report via sp_daily_pnl.
     */
    public List<Map<String, Object>>
        getDailyPnl(
            Date startDate, Date endDate
        ) throws LedgerException {
        LOG.info(
            "Running daily P&L report"
        );
        return reportDao.dailyPnl(
            startDate, endDate
        );
    }

    /**
     * Aging report via sp_aging_report.
     */
    public List<Map<String, Object>>
        getAgingReport()
        throws LedgerException {
        LOG.info(
            "Running aging report"
        );
        return reportDao.agingReport();
    }

    /**
     * Settlement report via
     * sp_settle_report.
     */
    public List<Map<String, Object>>
        getSettlementReport(
            Date startDate, Date endDate
        ) throws LedgerException {
        LOG.info(
            "Running settlement report"
        );
        return reportDao.settlementReport(
            startDate, endDate
        );
    }
}

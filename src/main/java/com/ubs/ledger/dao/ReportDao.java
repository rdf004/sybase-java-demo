package com.ubs.ledger.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc
    .core.JdbcTemplate;

import com.ubs.ledger.exception
    .LedgerException;

/**
 * DAO for reporting stored procedures.
 * Returns results as List of Maps because
 * report result sets have variable columns.
 *
 * Uses raw CallableStatement for all procs
 * because JdbcTemplate doesn't handle
 * Sybase multiple result sets well.
 *
 * @author Platform Engineering
 * @since 1.3
 */
public class ReportDao {

    private static final Logger LOG =
        Logger.getLogger(ReportDao.class);

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Execute sp_daily_pnl and return
     * results as list of maps.
     */
    public List<Map<String, Object>>
        dailyPnl(
            Date startDate, Date endDate
        ) throws LedgerException {
        return executeReportProc(
            "exec sp_daily_pnl ?, ?",
            new Object[]{
                new java.sql.Timestamp(
                    startDate.getTime()
                ),
                new java.sql.Timestamp(
                    endDate.getTime()
                )
            }
        );
    }

    /**
     * Execute sp_aging_report.
     */
    public List<Map<String, Object>>
        agingReport()
        throws LedgerException {
        return executeReportProc(
            "exec sp_aging_report", null
        );
    }

    /**
     * Execute sp_settle_report.
     */
    public List<Map<String, Object>>
        settlementReport(
            Date startDate, Date endDate
        ) throws LedgerException {
        Object[] params = null;
        String sql =
            "exec sp_settle_report";
        if (startDate != null
            && endDate != null) {
            sql += " ?, ?";
            params = new Object[]{
                new java.sql.Timestamp(
                    startDate.getTime()
                ),
                new java.sql.Timestamp(
                    endDate.getTime()
                )
            };
        }
        return executeReportProc(
            sql, params
        );
    }

    /**
     * Generic report proc executor.
     * Reads all columns from the result set
     * dynamically and returns as list of
     * maps.
     *
     * Handles Sybase CHAR padding by
     * trimming string values.
     */
    private List<Map<String, Object>>
        executeReportProc(
            String sql, Object[] params
        ) throws LedgerException {
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            conn = jdbcTemplate
                .getDataSource()
                .getConnection();
            cs = conn.prepareCall(sql);

            if (params != null) {
                for (int i = 0;
                    i < params.length;
                    i++) {
                    cs.setObject(
                        i + 1, params[i]
                    );
                }
            }

            boolean hasResults =
                cs.execute();

            List<Map<String, Object>>
                results = new ArrayList
                    <Map<String, Object>>();

            if (hasResults) {
                rs = cs.getResultSet();
                ResultSetMetaData meta =
                    rs.getMetaData();
                int colCount =
                    meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row =
                        new HashMap
                        <String, Object>();
                    for (int i = 1;
                        i <= colCount;
                        i++) {
                        String colName =
                            meta
                            .getColumnLabel(
                                i
                            );
                        Object val =
                            rs.getObject(i);
                        if (val
                            instanceof
                            String) {
                            val = ((String)
                                val).trim();
                        }
                        row.put(
                            colName, val
                        );
                    }
                    results.add(row);
                }
            }

            return results;
        } catch (SQLException e) {
            LOG.error(
                "Report proc failed: "
                + sql + " - "
                + e.getMessage()
            );
            throw new LedgerException(
                "Report execution failed",
                e,
                e.getErrorCode()
            );
        } finally {
            if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) {}
            }
            if (cs != null) {
                try { cs.close(); }
                catch (SQLException e) {}
            }
            if (conn != null) {
                try { conn.close(); }
                catch (SQLException e) {}
            }
        }
    }
}

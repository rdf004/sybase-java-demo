package com.ubs.ledger.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core
    .JdbcTemplate;
import org.springframework.stereotype
    .Repository;

import com.ubs.ledger.exception
    .LedgerException;

@Repository
public class ReportDao {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            ReportDao.class
        );

    private final JdbcTemplate jdbc;

    public ReportDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>>
        dailyPnl(
            Date startDate, Date endDate
        ) {
        return execReport(
            "{call sp_daily_pnl(?,?)}",
            startDate, endDate
        );
    }

    public List<Map<String, Object>>
        agingReport() {
        return execReport(
            "{call sp_aging_report}",
            null, null
        );
    }

    public List<Map<String, Object>>
        settlementReport(
            Date startDate, Date endDate
        ) {
        return execReport(
            "{call sp_settle_report(?,?)}",
            startDate, endDate
        );
    }

    private List<Map<String, Object>>
        execReport(
            String call,
            Date startDate,
            Date endDate
        ) {
        try (
            Connection conn = jdbc
                .getDataSource()
                .getConnection();
            CallableStatement cs =
                conn.prepareCall(call)
        ) {
            if (startDate != null) {
                cs.setTimestamp(1,
                    new Timestamp(
                        startDate.getTime()
                    )
                );
                cs.setTimestamp(2,
                    new Timestamp(
                        endDate.getTime()
                    )
                );
            }

            boolean has = cs.execute();
            var rows =
                new ArrayList<
                    Map<String, Object>
                >();

            while (has) {
                try (ResultSet rs =
                    cs.getResultSet()
                ) {
                    ResultSetMetaData md =
                        rs.getMetaData();
                    int cols =
                        md.getColumnCount();
                    while (rs.next()) {
                        var row =
                            new LinkedHashMap<
                                String,
                                Object
                            >();
                        for (
                            int i = 1;
                            i <= cols;
                            i++
                        ) {
                            row.put(
                                md.getColumnLabel(
                                    i
                                ).toLowerCase(),
                                rs.getObject(i)
                            );
                        }
                        rows.add(row);
                    }
                }
                has = cs.getMoreResults();
            }

            LOG.debug(
                "Report returned {} rows",
                rows.size()
            );
            return rows;
        } catch (SQLException e) {
            LOG.error(
                "Report failed: {}",
                e.getMessage()
            );
            throw new LedgerException(
                "Report execution failed",
                e,
                e.getErrorCode()
            );
        }
    }
}

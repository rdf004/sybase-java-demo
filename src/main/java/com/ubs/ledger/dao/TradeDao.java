package com.ubs.ledger.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc
    .core.JdbcTemplate;
import org.springframework.jdbc
    .core.RowMapper;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Trade;

/**
 * Data access object for trade table.
 * Uses JdbcTemplate for queries and raw
 * CallableStatement for stored proc calls.
 *
 * All SQL is Sybase-specific:
 * - Old-style joins (table1, table2 WHERE)
 * - rtrim() for CHAR column comparisons
 * - holdlock hints
 * - @@identity for last insert ID
 * - Sybase EXEC syntax for stored procs
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class TradeDao {

    private static final Logger LOG =
        Logger.getLogger(TradeDao.class);

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * RowMapper for the trade table.
     * Handles Sybase CHAR padding by
     * trimming all string columns.
     */
    private static final RowMapper<Trade>
        TRADE_ROW_MAPPER =
        new RowMapper<Trade>() {
        @Override
        public Trade mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Trade t = new Trade();
            t.setTradeId(
                rs.getLong("trade_id")
            );
            t.setTradeRef(
                trimNull(
                    rs.getString("trade_ref")
                )
            );
            t.setTradeDate(
                rs.getTimestamp("trade_date")
            );
            t.setSettleDate(
                rs.getTimestamp(
                    "settle_date"
                )
            );
            t.setValueDate(
                rs.getTimestamp("value_date")
            );
            t.setTraderId(
                rs.getLong("trader_id")
            );
            t.setCpId(
                rs.getLong("cp_id")
            );
            t.setInstrId(
                rs.getLong("instr_id")
            );
            t.setDirection(
                trimNull(
                    rs.getString("direction")
                )
            );
            t.setQuantity(
                rs.getBigDecimal("quantity")
            );
            t.setPrice(
                rs.getBigDecimal("price")
            );
            t.setNotional(
                rs.getBigDecimal("notional")
            );
            t.setCurrency(
                trimNull(
                    rs.getString("currency")
                )
            );
            t.setStatus(
                trimNull(
                    rs.getString("status")
                )
            );
            t.setAccruedInt(
                rs.getBigDecimal(
                    "accrued_int"
                )
            );
            t.setCreatedAt(
                rs.getTimestamp("created_at")
            );
            t.setUpdatedAt(
                rs.getTimestamp("updated_at")
            );
            return t;
        }
    };

    /**
     * RowMapper for trade with joined
     * counterparty/instrument/trader data.
     * Used by findWithDetails.
     */
    private static final RowMapper<Trade>
        TRADE_DETAIL_MAPPER =
        new RowMapper<Trade>() {
        @Override
        public Trade mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Trade t = new Trade();
            t.setTradeId(
                rs.getLong("trade_id")
            );
            t.setTradeRef(
                trimNull(
                    rs.getString("trade_ref")
                )
            );
            t.setTradeDate(
                rs.getTimestamp("trade_date")
            );
            t.setSettleDate(
                rs.getTimestamp(
                    "settle_date"
                )
            );
            t.setValueDate(
                rs.getTimestamp("value_date")
            );
            t.setTraderId(
                rs.getLong("trader_id")
            );
            t.setCpId(
                rs.getLong("cp_id")
            );
            t.setInstrId(
                rs.getLong("instr_id")
            );
            t.setDirection(
                trimNull(
                    rs.getString("direction")
                )
            );
            t.setQuantity(
                rs.getBigDecimal("quantity")
            );
            t.setPrice(
                rs.getBigDecimal("price")
            );
            t.setNotional(
                rs.getBigDecimal("notional")
            );
            t.setCurrency(
                trimNull(
                    rs.getString("currency")
                )
            );
            t.setStatus(
                trimNull(
                    rs.getString("status")
                )
            );
            t.setAccruedInt(
                rs.getBigDecimal(
                    "accrued_int"
                )
            );
            t.setCreatedAt(
                rs.getTimestamp("created_at")
            );
            t.setUpdatedAt(
                rs.getTimestamp("updated_at")
            );
            // joined fields
            t.setCpCode(
                trimNull(
                    rs.getString("cp_code")
                )
            );
            t.setCpName(
                trimNull(
                    rs.getString("cp_name")
                )
            );
            t.setTicker(
                trimNull(
                    rs.getString("ticker")
                )
            );
            t.setInstrName(
                trimNull(
                    rs.getString(
                        "instr_name"
                    )
                )
            );
            t.setEmpCode(
                trimNull(
                    rs.getString("emp_code")
                )
            );
            t.setTraderFirstName(
                trimNull(
                    rs.getString(
                        "first_name"
                    )
                )
            );
            t.setTraderLastName(
                trimNull(
                    rs.getString(
                        "last_name"
                    )
                )
            );
            return t;
        }
    };

    private static String trimNull(
        String s
    ) {
        return s == null ? null : s.trim();
    }

    /**
     * Find all trades. Uses SET ROWCOUNT
     * (Sybase-specific, no LIMIT clause).
     */
    public List<Trade> findAll(int maxRows)
        throws LedgerException {
        try {
            String sql =
                "SET ROWCOUNT "
                + maxRows
                + " SELECT * FROM trade"
                + " ORDER BY trade_date DESC"
                + " SET ROWCOUNT 0";
            return jdbcTemplate.query(
                sql, TRADE_ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findAll failed: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to list trades", e
            );
        }
    }

    /**
     * Search trades with filters.
     * Builds WHERE clause dynamically.
     *
     * NOTE: Uses string concatenation for
     * filter values. This is safe because
     * the controller validates input types,
     * but should be parameterized (FI-4055).
     */
    public List<Trade> search(
        Map<String, String> filters
    ) throws LedgerException {
        try {
            StringBuilder sql =
                new StringBuilder(
                    "SELECT * FROM trade"
                );
            StringBuilder where =
                new StringBuilder();

            if (filters.containsKey(
                "status"
            )) {
                where.append(
                    " AND rtrim(status)"
                    + " = '"
                    + filters.get("status")
                    + "'"
                );
            }
            if (filters.containsKey(
                "trader_id"
            )) {
                where.append(
                    " AND trader_id = "
                    + filters.get(
                        "trader_id"
                    )
                );
            }
            if (filters.containsKey(
                "cp_id"
            )) {
                where.append(
                    " AND cp_id = "
                    + filters.get("cp_id")
                );
            }
            if (filters.containsKey(
                "direction"
            )) {
                where.append(
                    " AND rtrim(direction)"
                    + " = '"
                    + filters.get(
                        "direction"
                    )
                    + "'"
                );
            }
            if (filters.containsKey(
                "currency"
            )) {
                where.append(
                    " AND rtrim(currency)"
                    + " = '"
                    + filters.get("currency")
                    + "'"
                );
            }

            if (where.length() > 0) {
                sql.append(" WHERE 1=1");
                sql.append(where);
            }
            sql.append(
                " ORDER BY trade_date DESC"
            );

            return jdbcTemplate.query(
                sql.toString(),
                TRADE_ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "search failed: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to search trades", e
            );
        }
    }

    /**
     * Find trade by ID with joined
     * counterparty, instrument, and trader
     * data. Uses Sybase old-style joins.
     */
    public Trade findWithDetails(
        long tradeId
    ) throws LedgerException {
        try {
            String sql =
                "SELECT t.*, "
                + "cp.cp_code, "
                + "cp.cp_name, "
                + "i.ticker, "
                + "i.instr_name, "
                + "tr.emp_code, "
                + "tr.first_name, "
                + "tr.last_name "
                + "FROM trade t, "
                + "counterparty cp, "
                + "instrument i, "
                + "trader tr "
                + "WHERE t.cp_id"
                + " = cp.cp_id "
                + "AND t.instr_id"
                + " = i.instr_id "
                + "AND t.trader_id"
                + " = tr.trader_id "
                + "AND t.trade_id = ?";

            List<Trade> results =
                jdbcTemplate.query(
                    sql,
                    new Object[]{tradeId},
                    TRADE_DETAIL_MAPPER
                );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } catch (Exception e) {
            LOG.error(
                "findWithDetails failed"
                + " for tradeId="
                + tradeId + ": "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get trade detail",
                e
            );
        }
    }

    /**
     * Book a new trade by calling the
     * sp_book_trade stored procedure.
     *
     * Uses raw JDBC CallableStatement
     * because Spring 3.2 SimpleJdbcCall
     * had issues with Sybase jConnect
     * metadata retrieval.
     *
     * @return the new trade_id from
     *         @@identity
     */
    public long bookTrade(
        String tradeRef,
        Date tradeDate,
        Date settleDate,
        long traderId,
        long cpId,
        long instrId,
        String direction,
        double quantity,
        double price,
        String currency,
        double accruedInt
    ) throws LedgerException {
        Connection conn = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            conn = jdbcTemplate
                .getDataSource()
                .getConnection();

            // Sybase EXEC syntax
            cs = conn.prepareCall(
                "exec sp_book_trade "
                + "?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?"
            );
            cs.setString(1, tradeRef);
            cs.setTimestamp(
                2,
                new java.sql.Timestamp(
                    tradeDate.getTime()
                )
            );
            cs.setTimestamp(
                3,
                new java.sql.Timestamp(
                    settleDate.getTime()
                )
            );
            cs.setLong(4, traderId);
            cs.setLong(5, cpId);
            cs.setLong(6, instrId);
            cs.setString(7, direction);
            cs.setDouble(8, quantity);
            cs.setDouble(9, price);
            cs.setString(10, currency);
            cs.setDouble(11, accruedInt);

            boolean hasResults =
                cs.execute();
            long newTradeId = -1;

            if (hasResults) {
                rs = cs.getResultSet();
                if (rs.next()) {
                    newTradeId = rs.getLong(
                        "new_trade_id"
                    );
                }
            }

            LOG.info(
                "Trade booked: "
                + tradeRef
                + " -> id=" + newTradeId
            );
            return newTradeId;
        } catch (SQLException e) {
            LOG.error(
                "sp_book_trade failed: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to book trade",
                e,
                e.getErrorCode()
            );
        } finally {
            closeQuietly(rs);
            closeQuietly(cs);
            closeQuietly(conn);
        }
    }

    /**
     * Match a trade via sp_match_trade.
     */
    public void matchTrade(
        long tradeId, String matchedBy
    ) throws LedgerException {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = jdbcTemplate
                .getDataSource()
                .getConnection();
            cs = conn.prepareCall(
                "exec sp_match_trade ?, ?"
            );
            cs.setLong(1, tradeId);
            cs.setString(2, matchedBy);
            cs.execute();

            LOG.info(
                "Trade matched: "
                + tradeId
                + " by " + matchedBy
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_match_trade failed"
                + " for " + tradeId
                + ": " + e.getMessage()
            );
            throw new LedgerException(
                "Failed to match trade",
                e,
                e.getErrorCode()
            );
        } finally {
            closeQuietly(cs);
            closeQuietly(conn);
        }
    }

    /**
     * Settle a trade via sp_settle_trade.
     */
    public void settleTrade(
        long tradeId, String settleMethod
    ) throws LedgerException {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = jdbcTemplate
                .getDataSource()
                .getConnection();
            cs = conn.prepareCall(
                "exec sp_settle_trade ?, ?"
            );
            cs.setLong(1, tradeId);
            cs.setString(2, settleMethod);
            cs.execute();

            LOG.info(
                "Trade settle initiated: "
                + tradeId
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_settle_trade failed"
                + " for " + tradeId
                + ": " + e.getMessage()
            );
            throw new LedgerException(
                "Failed to settle trade",
                e,
                e.getErrorCode()
            );
        } finally {
            closeQuietly(cs);
            closeQuietly(conn);
        }
    }

    /**
     * Retry a failed trade via
     * sp_retry_failed.
     */
    public void retryFailed(
        long tradeId, String settleMethod
    ) throws LedgerException {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            conn = jdbcTemplate
                .getDataSource()
                .getConnection();
            if (settleMethod != null) {
                cs = conn.prepareCall(
                    "exec sp_retry_failed"
                    + " ?, ?"
                );
                cs.setLong(1, tradeId);
                cs.setString(
                    2, settleMethod
                );
            } else {
                cs = conn.prepareCall(
                    "exec sp_retry_failed ?"
                );
                cs.setLong(1, tradeId);
            }
            cs.execute();

            LOG.info(
                "Trade retry queued: "
                + tradeId
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_retry_failed failed"
                + " for " + tradeId
                + ": " + e.getMessage()
            );
            throw new LedgerException(
                "Failed to retry trade",
                e,
                e.getErrorCode()
            );
        } finally {
            closeQuietly(cs);
            closeQuietly(conn);
        }
    }

    // JDBC resource cleanup helpers.
    // Java 7 try-with-resources would be
    // cleaner but we started this before
    // we were on Java 7. TODO: refactor.
    private void closeQuietly(
        ResultSet rs
    ) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    private void closeQuietly(
        CallableStatement cs
    ) {
        if (cs != null) {
            try {
                cs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    private void closeQuietly(
        Connection conn
    ) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}

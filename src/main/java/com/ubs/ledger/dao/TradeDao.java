package com.ubs.ledger.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core
    .JdbcTemplate;
import org.springframework.jdbc.core
    .RowMapper;
import org.springframework.stereotype
    .Repository;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Trade;

@Repository
public class TradeDao {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            TradeDao.class
        );

    private final JdbcTemplate jdbc;

    public TradeDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final
        RowMapper<Trade> ROW_MAPPER =
        (rs, rowNum) -> {
            var t = new Trade();
            t.setTradeId(
                rs.getLong("trade_id")
            );
            t.setTradeRef(
                trim(
                    rs.getString("trade_ref")
                )
            );
            t.setTradeDate(
                rs.getTimestamp("trade_date")
                    != null
                    ? rs.getTimestamp(
                        "trade_date"
                    ).toLocalDateTime()
                    : null
            );
            t.setSettleDate(
                rs.getTimestamp(
                    "settle_date"
                ) != null
                    ? rs.getTimestamp(
                        "settle_date"
                    ).toLocalDateTime()
                    : null
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
                trim(
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
                trim(
                    rs.getString("currency")
                )
            );
            t.setStatus(
                trim(
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
                    != null
                    ? rs.getTimestamp(
                        "created_at"
                    ).toLocalDateTime()
                    : null
            );
            t.setUpdatedAt(
                rs.getTimestamp("updated_at")
                    != null
                    ? rs.getTimestamp(
                        "updated_at"
                    ).toLocalDateTime()
                    : null
            );
            return t;
        };

    private static final
        RowMapper<Trade> DETAIL_MAPPER =
        (rs, rowNum) -> {
            Trade t =
                ROW_MAPPER.mapRow(rs, rowNum);
            t.setCpCode(
                trim(
                    rs.getString("cp_code")
                )
            );
            t.setCpName(
                trim(
                    rs.getString("cp_name")
                )
            );
            t.setTicker(
                trim(
                    rs.getString("ticker")
                )
            );
            t.setInstrName(
                trim(
                    rs.getString("instr_name")
                )
            );
            t.setEmpCode(
                trim(
                    rs.getString("emp_code")
                )
            );
            t.setTraderFirstName(
                trim(
                    rs.getString("first_name")
                )
            );
            t.setTraderLastName(
                trim(
                    rs.getString("last_name")
                )
            );
            return t;
        };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<Trade> findAll(int max) {
        String sql =
            "SELECT TOP(?) * FROM trade"
            + " ORDER BY trade_date DESC";
        return jdbc.query(
            sql, ROW_MAPPER, max
        );
    }

    public List<Trade> search(
        Map<String, String> filters
    ) {
        var sql = new StringBuilder(
            "SELECT * FROM trade"
        );
        var params =
            new ArrayList<Object>();
        var where = new StringBuilder();

        if (filters.containsKey("status")) {
            where.append(
                " AND status = ?"
            );
            params.add(
                filters.get("status")
            );
        }
        if (filters.containsKey(
            "trader_id"
        )) {
            where.append(
                " AND trader_id = ?"
            );
            params.add(Long.parseLong(
                filters.get("trader_id")
            ));
        }
        if (filters.containsKey("cp_id")) {
            where.append(
                " AND cp_id = ?"
            );
            params.add(Long.parseLong(
                filters.get("cp_id")
            ));
        }
        if (filters.containsKey(
            "direction"
        )) {
            where.append(
                " AND direction = ?"
            );
            params.add(
                filters.get("direction")
            );
        }
        if (filters.containsKey(
            "currency"
        )) {
            where.append(
                " AND currency = ?"
            );
            params.add(
                filters.get("currency")
            );
        }

        if (!where.isEmpty()) {
            sql.append(" WHERE 1=1");
            sql.append(where);
        }
        sql.append(
            " ORDER BY trade_date DESC"
        );

        return jdbc.query(
            sql.toString(),
            ROW_MAPPER,
            params.toArray()
        );
    }

    public Trade findWithDetails(
        long tradeId
    ) {
        String sql = """
            SELECT t.*,
                   cp.cp_code,
                   cp.cp_name,
                   i.ticker,
                   i.instr_name,
                   tr.emp_code,
                   tr.first_name,
                   tr.last_name
            FROM trade t
            JOIN counterparty cp
              ON t.cp_id = cp.cp_id
            JOIN instrument i
              ON t.instr_id = i.instr_id
            JOIN trader tr
              ON t.trader_id = tr.trader_id
            WHERE t.trade_id = ?""";

        List<Trade> results = jdbc.query(
            sql, DETAIL_MAPPER, tradeId
        );
        return results.isEmpty()
            ? null
            : results.getFirst();
    }

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
    ) {
        try (
            Connection conn = jdbc
                .getDataSource()
                .getConnection();
            CallableStatement cs =
                conn.prepareCall(
                    "{call sp_book_trade("
                    + "?,?,?,?,?,?,?,?,?,?,?"
                    + ")}"
                )
        ) {
            cs.setString(1, tradeRef);
            cs.setTimestamp(2,
                new Timestamp(
                    tradeDate.getTime()
                )
            );
            cs.setTimestamp(3,
                new Timestamp(
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

            boolean hasResult =
                cs.execute();
            long newId = -1;
            if (hasResult) {
                try (ResultSet rs =
                    cs.getResultSet()
                ) {
                    if (rs.next()) {
                        newId = rs.getLong(
                            "new_trade_id"
                        );
                    }
                }
            }
            LOG.info(
                "Trade booked: {} -> id={}",
                tradeRef, newId
            );
            return newId;
        } catch (SQLException e) {
            LOG.error(
                "sp_book_trade failed: {}",
                e.getMessage()
            );
            throw new LedgerException(
                "Failed to book trade",
                e,
                e.getErrorCode()
            );
        }
    }

    public void matchTrade(
        long tradeId, String matchedBy
    ) {
        try (
            Connection conn = jdbc
                .getDataSource()
                .getConnection();
            CallableStatement cs =
                conn.prepareCall(
                    "{call sp_match_trade"
                    + "(?,?)}"
                )
        ) {
            cs.setLong(1, tradeId);
            cs.setString(2, matchedBy);
            cs.execute();
            LOG.info(
                "Trade matched: {} by {}",
                tradeId, matchedBy
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_match_trade failed"
                + " for {}: {}",
                tradeId, e.getMessage()
            );
            throw new LedgerException(
                "Failed to match trade",
                e,
                e.getErrorCode()
            );
        }
    }

    public void settleTrade(
        long tradeId, String settleMethod
    ) {
        try (
            Connection conn = jdbc
                .getDataSource()
                .getConnection();
            CallableStatement cs =
                conn.prepareCall(
                    "{call sp_settle_trade"
                    + "(?,?)}"
                )
        ) {
            cs.setLong(1, tradeId);
            cs.setString(2, settleMethod);
            cs.execute();
            LOG.info(
                "Trade settle initiated: {}",
                tradeId
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_settle_trade failed"
                + " for {}: {}",
                tradeId, e.getMessage()
            );
            throw new LedgerException(
                "Failed to settle trade",
                e,
                e.getErrorCode()
            );
        }
    }

    public void retryFailed(
        long tradeId, String settleMethod
    ) {
        String call = settleMethod != null
            ? "{call sp_retry_failed(?,?)}"
            : "{call sp_retry_failed(?)}";

        try (
            Connection conn = jdbc
                .getDataSource()
                .getConnection();
            CallableStatement cs =
                conn.prepareCall(call)
        ) {
            cs.setLong(1, tradeId);
            if (settleMethod != null) {
                cs.setString(
                    2, settleMethod
                );
            }
            cs.execute();
            LOG.info(
                "Trade retry queued: {}",
                tradeId
            );
        } catch (SQLException e) {
            LOG.error(
                "sp_retry_failed failed"
                + " for {}: {}",
                tradeId, e.getMessage()
            );
            throw new LedgerException(
                "Failed to retry trade",
                e,
                e.getErrorCode()
            );
        }
    }
}

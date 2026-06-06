package com.ubs.ledger.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc
    .core.JdbcTemplate;
import org.springframework.jdbc
    .core.RowMapper;

import com.ubs.ledger.exception
    .LedgerException;
import com.ubs.ledger.model.Settlement;

/**
 * DAO for settlement table.
 *
 * @author Platform Engineering
 * @since 1.2
 */
public class SettlementDao {

    private static final Logger LOG =
        Logger.getLogger(
            SettlementDao.class
        );

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final
        RowMapper<Settlement> ROW_MAPPER =
        new RowMapper<Settlement>() {
        @Override
        public Settlement mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Settlement s = new Settlement();
            s.setSettleId(
                rs.getLong("settle_id")
            );
            s.setTradeId(
                rs.getLong("trade_id")
            );
            s.setAttemptNum(
                rs.getInt("attempt_num")
            );
            s.setSettleStatus(
                trim(
                    rs.getString(
                        "settle_status"
                    )
                )
            );
            s.setSettleMethod(
                trim(
                    rs.getString(
                        "settle_method"
                    )
                )
            );
            s.setSettledAmt(
                rs.getBigDecimal(
                    "settled_amt"
                )
            );
            s.setSettledAt(
                rs.getTimestamp(
                    "settled_at"
                )
            );
            s.setCreatedAt(
                rs.getTimestamp(
                    "created_at"
                )
            );
            return s;
        }
    };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<Settlement> findByTradeId(
        long tradeId
    ) throws LedgerException {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM settlement"
                + " WHERE trade_id = ?"
                + " ORDER BY attempt_num",
                new Object[]{tradeId},
                ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findByTradeId settlements"
                + " failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get settlements",
                e
            );
        }
    }
}

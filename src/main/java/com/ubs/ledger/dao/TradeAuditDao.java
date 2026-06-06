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
import com.ubs.ledger.model.TradeAudit;

/**
 * DAO for trade_audit table.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class TradeAuditDao {

    private static final Logger LOG =
        Logger.getLogger(
            TradeAuditDao.class
        );

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final
        RowMapper<TradeAudit> ROW_MAPPER =
        new RowMapper<TradeAudit>() {
        @Override
        public TradeAudit mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            TradeAudit a = new TradeAudit();
            a.setAuditId(
                rs.getLong("audit_id")
            );
            a.setTradeId(
                rs.getLong("trade_id")
            );
            a.setOldStatus(
                trim(
                    rs.getString(
                        "old_status"
                    )
                )
            );
            a.setNewStatus(
                trim(
                    rs.getString(
                        "new_status"
                    )
                )
            );
            a.setChangedBy(
                trim(
                    rs.getString(
                        "changed_by"
                    )
                )
            );
            a.setChangeReason(
                trim(
                    rs.getString(
                        "change_reason"
                    )
                )
            );
            a.setChangedAt(
                rs.getTimestamp(
                    "changed_at"
                )
            );
            return a;
        }
    };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<TradeAudit> findByTradeId(
        long tradeId
    ) throws LedgerException {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM trade_audit"
                + " WHERE trade_id = ?"
                + " ORDER BY changed_at",
                new Object[]{tradeId},
                ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findByTradeId audit"
                + " failed for "
                + tradeId + ": "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get audit trail",
                e
            );
        }
    }
}

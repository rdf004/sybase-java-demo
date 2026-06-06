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
import com.ubs.ledger.model.Trader;

/**
 * DAO for trader table.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class TraderDao {

    private static final Logger LOG =
        Logger.getLogger(TraderDao.class);

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final
        RowMapper<Trader> ROW_MAPPER =
        new RowMapper<Trader>() {
        @Override
        public Trader mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Trader t = new Trader();
            t.setTraderId(
                rs.getLong("trader_id")
            );
            t.setEmpCode(
                trim(
                    rs.getString("emp_code")
                )
            );
            t.setFirstName(
                trim(
                    rs.getString(
                        "first_name"
                    )
                )
            );
            t.setLastName(
                trim(
                    rs.getString(
                        "last_name"
                    )
                )
            );
            t.setDesk(
                trim(
                    rs.getString("desk")
                )
            );
            t.setRegion(
                trim(
                    rs.getString("region")
                )
            );
            t.setHireDate(
                rs.getTimestamp(
                    "hire_date"
                )
            );
            t.setDailyLimit(
                rs.getBigDecimal(
                    "daily_limit"
                )
            );
            t.setIsActive(
                rs.getInt("is_active")
            );
            t.setCreatedAt(
                rs.getTimestamp(
                    "created_at"
                )
            );
            return t;
        }
    };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<Trader> findAll()
        throws LedgerException {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM trader"
                + " ORDER BY emp_code",
                ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findAll traders: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to list traders", e
            );
        }
    }

    public Trader findById(long traderId)
        throws LedgerException {
        try {
            List<Trader> results =
                jdbcTemplate.query(
                    "SELECT * FROM trader"
                    + " WHERE"
                    + " trader_id = ?",
                    new Object[]{traderId},
                    ROW_MAPPER
                );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } catch (Exception e) {
            LOG.error(
                "findById trader: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get trader", e
            );
        }
    }
}

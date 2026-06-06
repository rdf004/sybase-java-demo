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
import com.ubs.ledger.model.Counterparty;

/**
 * DAO for counterparty table.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class CounterpartyDao {

    private static final Logger LOG =
        Logger.getLogger(
            CounterpartyDao.class
        );

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final
        RowMapper<Counterparty>
        ROW_MAPPER =
        new RowMapper<Counterparty>() {
        @Override
        public Counterparty mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Counterparty cp =
                new Counterparty();
            cp.setCpId(
                rs.getLong("cp_id")
            );
            cp.setCpCode(
                trim(
                    rs.getString("cp_code")
                )
            );
            cp.setCpName(
                trim(
                    rs.getString("cp_name")
                )
            );
            cp.setCpType(
                trim(
                    rs.getString("cp_type")
                )
            );
            cp.setLeiCode(
                trim(
                    rs.getString("lei_code")
                )
            );
            cp.setRegion(
                trim(
                    rs.getString("region")
                )
            );
            cp.setCreditLimit(
                rs.getBigDecimal(
                    "credit_limit"
                )
            );
            cp.setIsActive(
                rs.getInt("is_active")
            );
            cp.setCreatedAt(
                rs.getTimestamp(
                    "created_at"
                )
            );
            return cp;
        }
    };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<Counterparty> findAll()
        throws LedgerException {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM counterparty"
                + " ORDER BY cp_code",
                ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findAll CPs failed: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to list"
                + " counterparties", e
            );
        }
    }

    public Counterparty findById(long cpId)
        throws LedgerException {
        try {
            List<Counterparty> results =
                jdbcTemplate.query(
                    "SELECT * FROM"
                    + " counterparty"
                    + " WHERE cp_id = ?",
                    new Object[]{cpId},
                    ROW_MAPPER
                );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } catch (Exception e) {
            LOG.error(
                "findById CP failed"
                + " for " + cpId + ": "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get counterparty",
                e
            );
        }
    }
}

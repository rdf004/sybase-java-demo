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
import com.ubs.ledger.model.Instrument;

/**
 * DAO for instrument table.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class InstrumentDao {

    private static final Logger LOG =
        Logger.getLogger(
            InstrumentDao.class
        );

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(
        JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final
        RowMapper<Instrument> ROW_MAPPER =
        new RowMapper<Instrument>() {
        @Override
        public Instrument mapRow(
            ResultSet rs, int rowNum
        ) throws SQLException {
            Instrument i = new Instrument();
            i.setInstrId(
                rs.getLong("instr_id")
            );
            i.setTicker(
                trim(
                    rs.getString("ticker")
                )
            );
            i.setIsinCode(
                trim(
                    rs.getString("isin_code")
                )
            );
            i.setInstrName(
                trim(
                    rs.getString(
                        "instr_name"
                    )
                )
            );
            i.setInstrType(
                trim(
                    rs.getString(
                        "instr_type"
                    )
                )
            );
            i.setCurrency(
                trim(
                    rs.getString("currency")
                )
            );
            i.setExchange(
                trim(
                    rs.getString("exchange")
                )
            );
            i.setFaceValue(
                rs.getBigDecimal(
                    "face_value"
                )
            );
            i.setLotSize(
                rs.getInt("lot_size")
            );
            i.setIsActive(
                rs.getInt("is_active")
            );
            i.setCreatedAt(
                rs.getTimestamp(
                    "created_at"
                )
            );
            return i;
        }
    };

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    public List<Instrument> findAll()
        throws LedgerException {
        try {
            return jdbcTemplate.query(
                "SELECT * FROM instrument"
                + " ORDER BY ticker",
                ROW_MAPPER
            );
        } catch (Exception e) {
            LOG.error(
                "findAll instruments: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to list instruments",
                e
            );
        }
    }

    public Instrument findById(
        long instrId
    ) throws LedgerException {
        try {
            List<Instrument> results =
                jdbcTemplate.query(
                    "SELECT * FROM"
                    + " instrument WHERE"
                    + " instr_id = ?",
                    new Object[]{instrId},
                    ROW_MAPPER
                );
            if (results.isEmpty()) {
                return null;
            }
            return results.get(0);
        } catch (Exception e) {
            LOG.error(
                "findById instrument: "
                + e.getMessage()
            );
            throw new LedgerException(
                "Failed to get instrument",
                e
            );
        }
    }
}

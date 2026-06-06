package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Trade entity mapped to the trade table
 * in Sybase ASE (ubs_ledger database).
 *
 * No ORM annotations - this is a plain
 * POJO mapped manually in the DAO layer
 * via JdbcTemplate RowMapper.
 *
 * @author Platform Engineering
 * @since 1.0 (March 2012)
 */
public class Trade {

    private long tradeId;
    private String tradeRef;
    private Date tradeDate;
    private Date settleDate;
    private Date valueDate;
    private long traderId;
    private long cpId;
    private long instrId;
    private String direction;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal notional;
    private String currency;
    private String status;
    private BigDecimal accruedInt;
    private Date createdAt;
    private Date updatedAt;

    // joined fields from related tables
    private String cpCode;
    private String cpName;
    private String ticker;
    private String instrName;
    private String empCode;
    private String traderFirstName;
    private String traderLastName;

    public Trade() {
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getTradeRef() {
        return tradeRef;
    }

    public void setTradeRef(
        String tradeRef
    ) {
        this.tradeRef = tradeRef;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(
        Date tradeDate
    ) {
        this.tradeDate = tradeDate;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(
        Date settleDate
    ) {
        this.settleDate = settleDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(
        Date valueDate
    ) {
        this.valueDate = valueDate;
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(long traderId) {
        this.traderId = traderId;
    }

    public long getCpId() {
        return cpId;
    }

    public void setCpId(long cpId) {
        this.cpId = cpId;
    }

    public long getInstrId() {
        return instrId;
    }

    public void setInstrId(long instrId) {
        this.instrId = instrId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(
        String direction
    ) {
        this.direction = direction;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(
        BigDecimal quantity
    ) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(
        BigDecimal price
    ) {
        this.price = price;
    }

    public BigDecimal getNotional() {
        return notional;
    }

    public void setNotional(
        BigDecimal notional
    ) {
        this.notional = notional;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(
        String currency
    ) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAccruedInt() {
        return accruedInt;
    }

    public void setAccruedInt(
        BigDecimal accruedInt
    ) {
        this.accruedInt = accruedInt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
        Date createdAt
    ) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
        Date updatedAt
    ) {
        this.updatedAt = updatedAt;
    }

    public String getCpCode() {
        return cpCode;
    }

    public void setCpCode(String cpCode) {
        this.cpCode = cpCode;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getInstrName() {
        return instrName;
    }

    public void setInstrName(
        String instrName
    ) {
        this.instrName = instrName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getTraderFirstName() {
        return traderFirstName;
    }

    public void setTraderFirstName(
        String traderFirstName
    ) {
        this.traderFirstName =
            traderFirstName;
    }

    public String getTraderLastName() {
        return traderLastName;
    }

    public void setTraderLastName(
        String traderLastName
    ) {
        this.traderLastName =
            traderLastName;
    }

    @Override
    public String toString() {
        return "Trade{"
            + "tradeId=" + tradeId
            + ", tradeRef='" + tradeRef
            + "', status='" + status
            + "', notional=" + notional
            + '}';
    }
}

package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Instrument entity.
 * Mapped to instrument table in Sybase.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class Instrument {

    private long instrId;
    private String ticker;
    private String isinCode;
    private String instrName;
    private String instrType;
    private String currency;
    private String exchange;
    private BigDecimal faceValue;
    private int lotSize;
    private int isActive;
    private Date createdAt;

    public Instrument() {
    }

    public long getInstrId() {
        return instrId;
    }

    public void setInstrId(long instrId) {
        this.instrId = instrId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getIsinCode() {
        return isinCode;
    }

    public void setIsinCode(
        String isinCode
    ) {
        this.isinCode = isinCode;
    }

    public String getInstrName() {
        return instrName;
    }

    public void setInstrName(
        String instrName
    ) {
        this.instrName = instrName;
    }

    public String getInstrType() {
        return instrType;
    }

    public void setInstrType(
        String instrType
    ) {
        this.instrType = instrType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(
        String currency
    ) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(
        String exchange
    ) {
        this.exchange = exchange;
    }

    public BigDecimal getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(
        BigDecimal faceValue
    ) {
        this.faceValue = faceValue;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
        Date createdAt
    ) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Instrument{"
            + "instrId=" + instrId
            + ", ticker='" + ticker
            + "', instrName='" + instrName
            + "'}";
    }
}

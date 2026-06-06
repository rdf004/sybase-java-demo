package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Counterparty entity.
 * Mapped to counterparty table in Sybase.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class Counterparty {

    private long cpId;
    private String cpCode;
    private String cpName;
    private String cpType;
    private String leiCode;
    private String region;
    private BigDecimal creditLimit;
    private String notes;
    private int isActive;
    private Date createdAt;

    public Counterparty() {
    }

    public long getCpId() {
        return cpId;
    }

    public void setCpId(long cpId) {
        this.cpId = cpId;
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

    public String getCpType() {
        return cpType;
    }

    public void setCpType(String cpType) {
        this.cpType = cpType;
    }

    public String getLeiCode() {
        return leiCode;
    }

    public void setLeiCode(
        String leiCode
    ) {
        this.leiCode = leiCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(
        BigDecimal creditLimit
    ) {
        this.creditLimit = creditLimit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        return "Counterparty{"
            + "cpId=" + cpId
            + ", cpCode='" + cpCode
            + "', cpName='" + cpName
            + "'}";
    }
}

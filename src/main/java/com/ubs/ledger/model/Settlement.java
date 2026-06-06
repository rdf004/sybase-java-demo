package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Settlement entity.
 * Mapped to settlement table in Sybase.
 *
 * @author Platform Engineering
 * @since 1.2
 */
public class Settlement {

    private long settleId;
    private long tradeId;
    private int attemptNum;
    private String settleStatus;
    private String settleMethod;
    private BigDecimal settledAmt;
    private String failReason;
    private Date settledAt;
    private Date createdAt;

    public Settlement() {
    }

    public long getSettleId() {
        return settleId;
    }

    public void setSettleId(
        long settleId
    ) {
        this.settleId = settleId;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(
        int attemptNum
    ) {
        this.attemptNum = attemptNum;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(
        String settleStatus
    ) {
        this.settleStatus = settleStatus;
    }

    public String getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(
        String settleMethod
    ) {
        this.settleMethod = settleMethod;
    }

    public BigDecimal getSettledAmt() {
        return settledAmt;
    }

    public void setSettledAmt(
        BigDecimal settledAmt
    ) {
        this.settledAmt = settledAmt;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(
        String failReason
    ) {
        this.failReason = failReason;
    }

    public Date getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(
        Date settledAt
    ) {
        this.settledAt = settledAt;
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
        return "Settlement{"
            + "settleId=" + settleId
            + ", tradeId=" + tradeId
            + ", status='" + settleStatus
            + "'}";
    }
}

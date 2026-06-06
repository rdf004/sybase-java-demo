package com.ubs.ledger.model;

import java.util.Date;

/**
 * TradeAudit entity.
 * Mapped to trade_audit table in Sybase.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class TradeAudit {

    private long auditId;
    private long tradeId;
    private String oldStatus;
    private String newStatus;
    private String changedBy;
    private String changeReason;
    private Date changedAt;

    public TradeAudit() {
    }

    public long getAuditId() {
        return auditId;
    }

    public void setAuditId(long auditId) {
        this.auditId = auditId;
    }

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(
        String oldStatus
    ) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(
        String newStatus
    ) {
        this.newStatus = newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(
        String changedBy
    ) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(
        String changeReason
    ) {
        this.changeReason = changeReason;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(
        Date changedAt
    ) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "TradeAudit{"
            + "auditId=" + auditId
            + ", tradeId=" + tradeId
            + ", oldStatus='" + oldStatus
            + "', newStatus='" + newStatus
            + "'}";
    }
}

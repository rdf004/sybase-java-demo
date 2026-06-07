package com.ubs.ledger.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trade_audit")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "auditId", "tradeId",
    "oldStatus", "newStatus"
})
public class TradeAudit {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "new_status")
    private String newStatus;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "change_reason")
    private String changeReason;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}

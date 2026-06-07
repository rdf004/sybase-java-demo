package com.ubs.ledger.model;

import java.math.BigDecimal;
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
@Table(name = "settlement")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "settleId", "tradeId", "settleStatus"
})
public class Settlement {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "settle_id")
    private Long settleId;

    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "attempt_num")
    private Integer attemptNum;

    @Column(name = "settle_status")
    private String settleStatus;

    @Column(name = "settle_method")
    private String settleMethod;

    @Column(name = "settled_amt")
    private BigDecimal settledAmt;

    @Column(name = "fail_reason")
    private String failReason;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "tradeId", "tradeRef",
    "status", "notional"
})
public class Trade {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "trade_ref")
    private String tradeRef;

    @Column(name = "trade_date")
    private LocalDateTime tradeDate;

    @Column(name = "settle_date")
    private LocalDateTime settleDate;

    @Column(name = "value_date")
    private LocalDateTime valueDate;

    @Column(name = "trader_id")
    private Long traderId;

    @Column(name = "cp_id")
    private Long cpId;

    @Column(name = "instr_id")
    private Long instrId;

    @Column(name = "direction")
    private String direction;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "notional")
    private BigDecimal notional;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "accrued_int")
    private BigDecimal accruedInt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private String cpCode;

    @Transient
    private String cpName;

    @Transient
    private String ticker;

    @Transient
    private String instrName;

    @Transient
    private String empCode;

    @Transient
    private String traderFirstName;

    @Transient
    private String traderLastName;
}

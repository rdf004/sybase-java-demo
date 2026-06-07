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
@Table(name = "instrument")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "instrId", "ticker", "instrName"
})
public class Instrument {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "instr_id")
    private Long instrId;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "isin_code")
    private String isinCode;

    @Column(name = "instr_name")
    private String instrName;

    @Column(name = "instr_type")
    private String instrType;

    @Column(name = "currency")
    private String currency;

    @Column(name = "exchange")
    private String exchange;

    @Column(name = "face_value")
    private BigDecimal faceValue;

    @Column(name = "lot_size")
    private Integer lotSize;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

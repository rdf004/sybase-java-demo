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
@Table(name = "counterparty")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "cpId", "cpCode", "cpName"
})
public class Counterparty {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "cp_id")
    private Long cpId;

    @Column(name = "cp_code")
    private String cpCode;

    @Column(name = "cp_name")
    private String cpName;

    @Column(name = "cp_type")
    private String cpType;

    @Column(name = "lei_code")
    private String leiCode;

    @Column(name = "region")
    private String region;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

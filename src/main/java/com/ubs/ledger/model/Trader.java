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
@Table(name = "trader")
@Getter
@Setter
@NoArgsConstructor
@ToString(of = {
    "traderId", "empCode",
    "firstName", "lastName"
})
public class Trader {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "trader_id")
    private Long traderId;

    @Column(name = "emp_code")
    private String empCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "desk")
    private String desk;

    @Column(name = "region")
    private String region;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

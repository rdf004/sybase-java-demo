package com.ubs.ledger.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Trader entity.
 * Mapped to trader table in Sybase.
 *
 * @author Platform Engineering
 * @since 1.0
 */
public class Trader {

    private long traderId;
    private String empCode;
    private String firstName;
    private String lastName;
    private String desk;
    private String region;
    private Date hireDate;
    private BigDecimal dailyLimit;
    private int isActive;
    private Date createdAt;

    public Trader() {
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(
        long traderId
    ) {
        this.traderId = traderId;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(
        String empCode
    ) {
        this.empCode = empCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(
        String firstName
    ) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(
        String lastName
    ) {
        this.lastName = lastName;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(
        Date hireDate
    ) {
        this.hireDate = hireDate;
    }

    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(
        BigDecimal dailyLimit
    ) {
        this.dailyLimit = dailyLimit;
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
        return "Trader{"
            + "traderId=" + traderId
            + ", empCode='" + empCode
            + "', name='" + firstName
            + " " + lastName
            + "'}";
    }
}

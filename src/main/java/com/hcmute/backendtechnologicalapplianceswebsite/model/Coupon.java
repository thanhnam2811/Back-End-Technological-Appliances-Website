package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "Coupons")
public class Coupon implements Serializable {
    @Id
    @Column(name = "CouponId", nullable = false, length = 40)
    private String couponId;

    @Column(name = "Discount", nullable = false)
    private Double discount;

    @Column(name = "ExpiredTime")
    private Instant expiredTime;

    @Column(name = "EffectiveTime")
    private Instant effectiveTime;

    @Column(name = "Description", length = 300)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Instant effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Instant getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Instant expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String id) {
        this.couponId = id;
    }
}
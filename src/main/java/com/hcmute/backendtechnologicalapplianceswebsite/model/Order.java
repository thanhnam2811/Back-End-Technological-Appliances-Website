package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @Column(name = "OrderId", nullable = false, length = 20)
    private String orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Username")
    private User username;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Address", nullable = false, length = 200)
    private String address;

    @Column(name = "PhoneNumer", nullable = false, length = 20)
    private String phoneNumer;

    @Column(name = "PurchaseDate")
    private Instant purchaseDate;

    @Column(name = "TotalPrices")
    private Double totalPrices;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DeliveryId")
    private Delivery delivery;

    @Column(name = "Status", length = 200)
    private String status;

    @Column(name = "CouponId")
    private Integer couponId;

    @Column(name = "DiscountPrice")
    private Double discountPrice;

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Double getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(Double totalPrices) {
        this.totalPrices = totalPrices;
    }

    public Instant getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Instant purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPhoneNumer() {
        return phoneNumer;
    }

    public void setPhoneNumer(String phoneNumer) {
        this.phoneNumer = phoneNumer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String id) {
        this.orderId = id;
    }
}
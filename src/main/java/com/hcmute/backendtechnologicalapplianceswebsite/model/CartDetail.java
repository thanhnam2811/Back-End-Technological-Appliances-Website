package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.*;

@Entity
@Table(name = "CartDetails")
public class CartDetail {
    @EmbeddedId
    private CartDetailId id;

    @MapsId("username")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Username", nullable = false)
    private User username;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

    @Column(name = "Quantity", nullable = false)
    private Integer quantity;

    @MapsId("username")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Username")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUsername() {
        return username;
    }

    public void setUsername(User username) {
        this.username = username;
    }

    public CartDetailId getId() {
        return id;
    }

    public void setId(CartDetailId id) {
        this.id = id;
    }
}
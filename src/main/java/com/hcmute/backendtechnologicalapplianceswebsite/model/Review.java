package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "Reviews")
public class Review  implements Serializable {
    @Id
    @Column(name = "ReviewId", nullable = false, length = 20)
    private String reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Username")
    private User username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductId")
    private Product product;

    @Lob
    @Column(name = "Content")
    private String content;

    @Column(name = "Rate")
    private Integer rate;

    @Column(name = "\"Time\"")
    private Instant time;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String id) {
        this.reviewId = id;
    }
}
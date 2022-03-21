package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @Column(name = "CategoryId", nullable = false, length = 20)
    private String categoryId;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String id) {
        this.categoryId = id;
    }
}
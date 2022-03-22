package com.hcmute.backendtechnologicalapplianceswebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Account")
public class Account implements Serializable {
    @Id
    @Column(name = "Username", nullable = false, length = 40)
    private String username;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Username", nullable = false)
    private User user;

    @Column(name = "Password", nullable = false, length = 40)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User users) {
        this.user = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }
}
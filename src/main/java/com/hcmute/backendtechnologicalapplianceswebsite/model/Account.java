package com.hcmute.backendtechnologicalapplianceswebsite.model;

import javax.persistence.*;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @Column(name = "Username", nullable = false, length = 40)
    private String username;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "Username", nullable = false)
    private User users;

    @Column(name = "Password", nullable = false, length = 40)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String id) {
        this.username = id;
    }
}
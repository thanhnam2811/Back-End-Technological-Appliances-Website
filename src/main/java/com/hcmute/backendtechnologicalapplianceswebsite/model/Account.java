package com.hcmute.backendtechnologicalapplianceswebsite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "Account")
public class Account implements Serializable, UserDetails {
    @Id
    @Column(name = "Username", nullable = false, length = 40)
    private String username;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "Username", nullable = false)
    private User user;

    @JsonIgnore
    @Column(name = "Password", nullable = false, length = 40)
    private String password;

    public static Account build(org.springframework.security.core.userdetails.User user) {
        Account account = new Account();
        account.setUsername(user.getUsername());
        account.setPassword(user.getPassword());
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

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

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setUsername(String id) {
        this.username = id;
    }

    @Override
    public String toString() {
        return "Account: username=" + username + ", password=" + password;
    }
}
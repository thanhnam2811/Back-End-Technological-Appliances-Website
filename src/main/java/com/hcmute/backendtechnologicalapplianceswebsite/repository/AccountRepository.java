package com.hcmute.backendtechnologicalapplianceswebsite.repository;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByUsername(String username);
}
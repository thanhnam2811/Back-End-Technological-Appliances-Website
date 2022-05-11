package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.AccountRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class AccountController {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountController(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // Get All Accounts
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    //    Create account
    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account account) {
        User user = userRepository.findById(account.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + account.getUsername()));
        account.setUser(user);
        accountRepository.save(account);
        user.setAccount(account);
        userRepository.save(user);
        return account;
    }

    //    Get account by username
    @GetMapping("/accounts/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        Account account =  accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + username));
        return ResponseEntity.ok(account);
    }

    //    Update account
    @PutMapping("/accounts/{username}")
    public ResponseEntity<Account> updateAccount(@PathVariable String username, @RequestBody Account account) throws Throwable {
        Account _account = accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + username));
        _account.setPassword(account.getPassword());
        accountRepository.save(_account);
        return ResponseEntity.ok(_account);
    }

    //    Delete account
    @DeleteMapping("/accounts/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable String username) {
        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + username));
        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
}

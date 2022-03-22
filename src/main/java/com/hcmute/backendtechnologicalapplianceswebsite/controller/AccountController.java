package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/technological_appliances/")
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /* PRIVATE
    // Get All Accounts
    @GetMapping("accounts")
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    */

    //    Create account
    //    NOTE: Còn lỗi!!!
    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account account) {

        return accountRepository.save(account);
    }

    /* PRIVATE
    //    Get account by username
    @GetMapping("/accounts/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        Account account =  accountRepository.findByUsername(username);
        if (account == null)
            throw new ResourceNotFoundException("Account not found with username: " + username);
        return ResponseEntity.ok(account);
    }
    */

    /* PRIVATE
    //    Update account
    @PutMapping("/accounts/{username}")
    public ResponseEntity<Account> updateAccount(@PathVariable String username, @RequestBody Account account) throws Throwable {
        Account _account = (Account) accountRepository.findByUsername(username);
        if (_account == null)
            throw new ResourceNotFoundException("Account not found with username: " + username);
        _account.setPassword(account.getPassword());
        accountRepository.save(_account);
        return ResponseEntity.ok(_account);
    }
    */

    /* PRIVATE
    //    Delete account
    @DeleteMapping("/accounts/{username}")
    public ResponseEntity<?> deleteAccount(@PathVariable String username) {
        Account account = (Account) accountRepository.findByUsername(username);
        if (account == null)
            throw new ResourceNotFoundException("Account not found with username: " + username);
        accountRepository.delete(account);
        return ResponseEntity.ok().build();
    }
    */
}

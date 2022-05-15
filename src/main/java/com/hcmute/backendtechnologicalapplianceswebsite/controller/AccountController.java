package com.hcmute.backendtechnologicalapplianceswebsite.controller;

import com.hcmute.backendtechnologicalapplianceswebsite.exception.ResourceNotFoundException;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Mail;
import com.hcmute.backendtechnologicalapplianceswebsite.model.PasswordResetToken;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.AccountRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.PasswordResetTokenRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.service.EmailService;
import com.hcmute.backendtechnologicalapplianceswebsite.service.ISecurityUserServiceImpl;
import com.hcmute.backendtechnologicalapplianceswebsite.utils.MyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
@RestController
@RequestMapping("/api/technological_appliances/")
public class AccountController {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ISecurityUserServiceImpl securityUserService;
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountController(AccountRepository accountRepository, UserRepository userRepository, ISecurityUserServiceImpl securityUserService, EmailService emailService, PasswordResetTokenRepository passwordTokenRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.securityUserService = securityUserService;
        this.emailService = emailService;
        this.passwordTokenRepository = passwordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/forgot-password/{username}")
    public String forgotPassword(@PathVariable String username, @RequestParam String email) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        if (!user.getEmail().equals(email)) {
            throw new ResourceNotFoundException("Email is not correct");
        } else {
            String token = MyUtils.generateToken();
            securityUserService.createPasswordResetTokenForUser(user, token);
            Mail mail = new Mail();
            mail.setFrom("no-reply@webtechappliances.com");
            mail.setTo(user.getEmail());
            mail.setSubject("Password reset request");

            Map<String, Object> model = new HashMap<>();
            model.put("user", user);
            String resetUrl = "http://localhost:3000/reset-password?token=" + token;
            model.put("resetUrl", resetUrl);
            model.put("signature", "From WebTechAppliances");
            model.put("WEBSITE_NAME", "Web Tech Appliances");
            mail.setModel(model);
            emailService.sendEmail(mail);

            log.info("Send email to " + user.getEmail());
            return "Email has been sent";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password) {
        String result = securityUserService.validatePasswordResetToken(token);
        if (result != null) {
            throw new ResourceNotFoundException("Token is not valid");
        } else {
            PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
            Account account = accountRepository.findById(passToken.getUser().getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + passToken.getUser().getUsername()));
            String hashPassword = passwordEncoder.encode(password);
            account.setPassword(hashPassword);
            accountRepository.save(account);

            log.info("Reset password for " + account.getUsername());
            return "Password has been reset";
        }
    }

    @PostMapping("/change-password/{username}")
    public String changePassword(@PathVariable String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        Account account = accountRepository.findById(username)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with username: " + username));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new ResourceNotFoundException("Old password is not correct");
        } else {
            String hashPassword = passwordEncoder.encode(newPassword);
            account.setPassword(hashPassword);
            accountRepository.save(account);

            log.info("Change password for " + account.getUsername());
            return "Password has been changed";
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestHeader String username, @RequestHeader String password, @RequestBody User user) {
        if (!username.equals(user.getUsername())) {
            user.setUsername(username);
        }
        if (userRepository.existsById(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is existed");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is existed");
        }
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone number is existed");
        }
        User newUser = userRepository.save(user);
        Account account = new Account();
        account.setUsername(newUser.getUsername());
        account.setPassword(passwordEncoder.encode(password));
        account.setUser(newUser);
        account.setRole(Account.ROLE_USER);
        accountRepository.save(account);
        return ResponseEntity.ok(newUser);
    }

}

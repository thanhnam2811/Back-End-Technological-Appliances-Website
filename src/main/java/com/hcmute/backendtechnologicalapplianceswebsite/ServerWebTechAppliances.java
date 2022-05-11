package com.hcmute.backendtechnologicalapplianceswebsite;

import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import com.hcmute.backendtechnologicalapplianceswebsite.model.User;
import com.hcmute.backendtechnologicalapplianceswebsite.repository.UserRepository;
import com.hcmute.backendtechnologicalapplianceswebsite.service.AccountService;
import com.hcmute.backendtechnologicalapplianceswebsite.service.AccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
@Slf4j
public class ServerWebTechAppliances {

    public static void main(String[] args) {
        SpringApplication.run(ServerWebTechAppliances.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package com.hcmute.backendtechnologicalapplianceswebsite.security;

import com.hcmute.backendtechnologicalapplianceswebsite.filter.CustomAuthenticationFilter;
import com.hcmute.backendtechnologicalapplianceswebsite.filter.CustomAuthorizationFilter;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String ROLE_ADMIN = Account.getRoleName(Account.ROLE_ADMIN);
        String ROLE_USER = Account.getRoleName(Account.ROLE_USER);

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/technological_appliances/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/api/technological_appliances/**").permitAll();

        http.authorizeRequests()
                .antMatchers("/api/technological_appliances/cart-details/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .antMatchers("/api/technological_appliances/order-details/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .antMatchers("/api/technological_appliances/orders/**").hasAnyRole(ROLE_USER, ROLE_ADMIN)
                .antMatchers(GET, "/api/technological_appliances/users").hasAnyAuthority(ROLE_ADMIN)
                .antMatchers(GET, "/api/technological_appliances/users/{username}").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
                .antMatchers(GET, "/api/technological_appliances/users/{username}/role").hasAnyAuthority(ROLE_ADMIN)
                .antMatchers(GET, "/api/technological_appliances/account").hasAnyAuthority(ROLE_ADMIN);

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

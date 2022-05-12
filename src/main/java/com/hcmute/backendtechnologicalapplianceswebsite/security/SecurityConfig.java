package com.hcmute.backendtechnologicalapplianceswebsite.security;

import com.hcmute.backendtechnologicalapplianceswebsite.filter.CustomAuthenticationFilter;
import com.hcmute.backendtechnologicalapplianceswebsite.filter.CustomAuthorizationFilter;
import com.hcmute.backendtechnologicalapplianceswebsite.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@Order(1)
@RequiredArgsConstructor
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
        String apiPath = "/api/technological_appliances";
        String apiPathCartDetail = "/api/cart-details";
        String apiPathOrder = "/api/orders";
        String apiPathOrderDetail = "/api/order-details";

        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl(apiPath + "/login");

        http.csrf().disable()
                .cors().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        // ROLE_USER
        http.authorizeRequests()
                .antMatchers(apiPathCartDetail + "/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
                .antMatchers(apiPathOrder + "/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN)
                .antMatchers(apiPathOrderDetail + "/**").hasAnyAuthority(ROLE_USER, ROLE_ADMIN);

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

package com.godev.linkhubservice.config;

import com.godev.linkhubservice.services.impl.AccountServiceImpl;
import com.godev.linkhubservice.security.jwt.JwtAuthFilter;
import com.godev.linkhubservice.security.jwt.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountServiceImpl accountService;
    private final JwtService jwtService;

    public SecurityConfig(@Lazy AccountServiceImpl accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OncePerRequestFilter jwtFilter() {

        return new JwtAuthFilter(jwtService, accountService);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/account")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/account/auth")
                        .permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(this.jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}

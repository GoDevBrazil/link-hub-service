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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/account")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/account/auth")
                        .permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                    .requestMatchers(HttpMethod.POST, "/page/view")
                        .permitAll()
                    .anyRequest().authenticated()
                .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(this.jwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Permitir solicitações de todos os origens (não recomendado para produção)
        configuration.addAllowedMethod("*"); // Permitir todos os métodos HTTP (GET, POST, etc.)
        configuration.addAllowedHeader("*"); // Permitir todos os cabeçalhos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

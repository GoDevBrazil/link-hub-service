package com.godev.linkhubservice.security.jwt;

import com.godev.linkhubservice.domain.services.impl.AccountServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final AccountServiceImpl accountService;

    public JwtAuthFilter(JwtService jwtService, AccountServiceImpl accountService) {
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        var authorization = request.getHeader("authorization");

        if(!ObjectUtils.isEmpty(authorization) && authorization.startsWith("Bearer")) {

            var token = authorization.split(" ")[1];

            if (jwtService.isValidToken(token)) {

                var loggedAccount = jwtService.getLoggedAccount(token);
                var userDetails = accountService.loadUserByUsername(loggedAccount);
                var user = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }
        filterChain.doFilter(request, response);
    }
}

package com.godev.linkhubservice.security.jwt;

import com.godev.linkhubservice.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.util.Date.from;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(String email) {
        var exp = this.jwtProperties.getExpiration();
        var dateTimeExpiration = LocalDateTime.now().plusHours(exp);
        var instant = dateTimeExpiration.atZone(ZoneId.systemDefault()).toInstant();
        var date = from(instant);

        var key = Keys.hmacShaKeyFor(this.jwtProperties.getSignKey().getBytes(StandardCharsets.UTF_8));

        return Jwts
                .builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {

        var key = Keys.hmacShaKeyFor(this.jwtProperties.getSignKey().getBytes(StandardCharsets.UTF_8));

        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token) {
        try {
            var claims = getClaims(token);
            var expirationDate = claims.getExpiration();
            var date = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return LocalDateTime.now().isBefore(date);
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedAccount(String token) {
        return getClaims(token).getSubject();
    }

}



package org.example.user.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService implements TokenRepository {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Getter
    @Value("${security.jwt.expiration-date}")
    private Long expirationDate;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String buildToken(
            Map<String, Object> claims,
            User user
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(secretKey)
                .compact();
    }

    private <T> T extractClaims(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(
            String token
    ) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public String generateToken(
            User user
    ) {
        return buildToken(new HashMap<>(), user);
    }

    @Override
    public boolean isTokenValid(
            String token,
            User user
    ) {
        try {
            return getUsernameFromToken(token).equals(user.getUsername()) && !isTokenExpired(token);
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(
            String token
    ) {
        return extractClaims(token, Claims::getSubject);
    }
}

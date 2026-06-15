package org.example.shared.infrastructure.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthenticationRepository authenticationRepository;
    private static final String BEARER_PREFIX = "Bearer ";

    public JwtAuthFilter(
           JwtService jwtService,
           AuthenticationRepository authenticationRepository
    ) {
        this.jwtService = jwtService;
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, JwtException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replaceFirst(BEARER_PREFIX, "");

        try {
            String username = jwtService.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticationRepository.findByUsername(username)
                        .filter(user -> jwtService.isTokenValid(token, user))
                        .ifPresent(this::setAuthenticationContext);
            }
        } catch (JwtException exception) {
            throw new JwtException("The JWT Token is invalid, expired or malformed.", exception);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationContext(
            User user
    ) {
        CustomUserDetails userDetails = new CustomUserDetails(user);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}

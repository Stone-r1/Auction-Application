package org.example.user.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replaceFirst(BEARER_PREFIX, "");
        String username = jwtService.getUsernameFromToken(token);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        User user = authenticationRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with username " + username + " does not exist"
                ));

        if (jwtService.isTokenValid(token, user)) {
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            // No authorities as for now
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
}

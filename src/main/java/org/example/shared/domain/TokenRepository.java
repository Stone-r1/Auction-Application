package org.example.shared.domain;

import org.example.user.domain.entities.User;


public interface TokenRepository {
    String generateToken(User user);
    boolean isTokenValid(String token, User user);
    String getUsernameFromToken(String token);
}

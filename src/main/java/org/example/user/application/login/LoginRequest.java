package org.example.user.application.login;


public record LoginRequest(
        String username,
        String password
) {}

package org.example.user.application.register;

import java.time.LocalDate;


public record RegisterRequest(
        String username,
        String password,
        String email,
        LocalDate birthDate
) {}

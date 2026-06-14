package org.example.user.application.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


public record RegisterRequest(

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must contain at least one upper-case letter, one lower-case letter, one digit," +
                        " one special character and minimum length should be more than 8 characters long")
        String password,

        @NotBlank(message = "Email is required")
        @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Email format is invalid")
        String email,

        @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd",
                iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate
) {}

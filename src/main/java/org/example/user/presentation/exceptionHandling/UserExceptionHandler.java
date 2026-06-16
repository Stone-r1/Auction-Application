package org.example.user.presentation.exceptionHandling;


import org.example.shared.infrastructure.exceptions.EmailDeliveryException;
import org.example.shared.presentation.exceptionHandlers.ErrorMessage;
import org.example.user.domain.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.Map;


@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleUserAlreadyExists(
            UserAlreadyExistsException exception
    ) {
        return new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                new Date(),
                "User already exists",
                Map.of(exception.getField(), exception.getValue())
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleUsernameNotFound(
            UsernameNotFoundException exception
    ) {
        // Intentionally generic to not confirm whether a username exists (user enumeration)
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Authentication failed",
                Map.of("reason", "Invalid username or password.")
        );
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorMessage handleBadCredentials(
            AuthenticationCredentialsNotFoundException exception
    ) {
        // Intentionally generic to not expose whether username or password was wrong
        return new ErrorMessage(
                HttpStatus.UNAUTHORIZED.value(),
                new Date(),
                "Authentication failed",
                Map.of("reason", "Invalid username or password.")
        );
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleDisabledAccount(
            DisabledException exception
    ) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                "Account not verified",
                Map.of("action", "Check your email for the verification link.")
        );
    }

    @ExceptionHandler(EmailDeliveryException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessage handleEmailDeliveryFailure(
            EmailDeliveryException exception
    ) {
        return new ErrorMessage(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                new Date(),
                "Email delivery failed",
                Map.of("action", "Registration could not be completed. Please try again later.")
        );
    }
}

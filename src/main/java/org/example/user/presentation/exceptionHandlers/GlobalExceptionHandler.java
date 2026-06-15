package org.example.user.presentation.exceptionHandlers;


import jakarta.persistence.EntityExistsException;
import org.example.user.infrastructure.exceptions.EmailDeliveryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
                );

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleParseError(HttpMessageNotReadableException exception) {

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Invalid request format",
                Map.of("Date Format", "Date must be in format yyyy-MM-dd")
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleEntityExists(EntityExistsException exception) {

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Invalid credentials",
                Map.of("User already exists", exception.getLocalizedMessage())
        );
    }

    @ExceptionHandler(EmailDeliveryException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorMessage handleEmailDeliveryFailure(EmailDeliveryException exception) {

        return new ErrorMessage(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                new Date(),
                "Could not send verification email",
                Map.of("Email", "Registration could not be completed because the verification " +
                        "email failed to send. Please try registering again.")
        );
    }
}

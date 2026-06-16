package org.example.shared.presentation.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/*
 * Centralized handler for cross-cutting exceptions that apply to all controllers.
 * Domain-specific exceptions are handled in their respective module handlers.
 */
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
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleParseError(
            HttpMessageNotReadableException exception
    ) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                "Invalid request body",
                Map.of("hint", "Check that all fields are present and correctly formatted (e.g. dates as yyyy-MM-dd).")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleAccessDenied(
            AccessDeniedException exception
    ) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                new Date(),
                "Access denied",
                Map.of("reason", "You do not have permission to perform this action.")
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleUnexpected(
            Exception exception
    ) {
        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                "An unexpected error occurred",
                Map.of("detail", exception.getMessage() != null ? exception.getMessage() : "No further details available.")
        );
    }
}


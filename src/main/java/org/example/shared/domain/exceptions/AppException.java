package org.example.shared.domain.exceptions;

/*
 * Base class for all application-level domain exceptions.
 * Centralizes exception chaining support and consistent message formatting.
 */
public abstract class AppException extends RuntimeException {

    protected AppException(
            String message
    ) {
        super(message);
    }

    protected AppException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}


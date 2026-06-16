package org.example.shared.infrastructure.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class EmailDeliveryException extends AppException {

    public EmailDeliveryException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}

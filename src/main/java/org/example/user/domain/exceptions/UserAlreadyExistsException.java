package org.example.user.domain.exceptions;

import lombok.Getter;
import org.example.shared.domain.exceptions.AppException;


@Getter
public class UserAlreadyExistsException extends AppException {

    private final String field;
    private final String value;

    public UserAlreadyExistsException(
            String field,
            String value
    ) {
        super("A user with " + field + " '" + value + "' already exists.");
        this.field = field;
        this.value = value;
    }

    public UserAlreadyExistsException(
            String field,
            String value,
            Throwable cause
    ) {
        super("A user with " + field + " '" + value + "' already exists.", cause);
        this.field = field;
        this.value = value;
    }
}

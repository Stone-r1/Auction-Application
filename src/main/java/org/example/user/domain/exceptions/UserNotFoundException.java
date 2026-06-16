package org.example.user.domain.exceptions;

import org.example.shared.domain.exceptions.AppException;


public class UserNotFoundException extends AppException {

    public UserNotFoundException(
            String username
    ) {
        super("User with username '" + username + "' was not found.");
    }

    public UserNotFoundException(
            String username,
            Throwable cause
    ) {
        super("User with username '" + username + "' was not found.", cause);
    }
}


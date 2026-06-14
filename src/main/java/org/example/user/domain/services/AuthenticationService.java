package org.example.user.domain.services;


import jakarta.persistence.EntityExistsException;
import org.example.user.domain.entities.User;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationService(
            AuthenticationRepository authenticationRepository
    ) {
        this.authenticationRepository = authenticationRepository;
    }

    private void validateUserDoesNotExist(
            User userToCheck
    ) {
        User user = authenticationRepository
                .findByUsername(userToCheck.getUsername())
                .orElse(null);

        if (user != null) {
            throw new EntityExistsException(
                    "User with username " + userToCheck.getUsername() + " already exists"
            );
        }

        user = authenticationRepository
                .findByEmail(userToCheck.getEmail())
                .orElse(null);

        if (user != null) {
            throw new EntityExistsException(
                    "User with email " + userToCheck.getEmail() + " already exists"
            );
        }
    }

    public User getUserByUsername(
            String username,
            String password
    ) {
        User user = authenticationRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User with username: " + username + " Was not found."
                        )
                );

        if (!user.getPassword().equals(password)) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Wrong password for the user with username " + username
            );
        }

        return user;
    }

    // TODO: validate email exists and password is secure
    public User registerUser(
            User userToRegister
    ) {
        validateUserDoesNotExist(userToRegister);
        return authenticationRepository.save(userToRegister);
    }
}

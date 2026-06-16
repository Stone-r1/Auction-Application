package org.example.user.domain.services;


import org.example.user.domain.entities.User;
import org.example.user.domain.exceptions.UserAlreadyExistsException;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationRepository authenticationRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationRepository = authenticationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void throwUserExists(
            String field,
            String value
    ) {
        throw new UserAlreadyExistsException(field, value);
    }

    private void validateUserDoesNotExist(
            User userToCheck
    ) {
        String username = userToCheck.getUsername();
        String email = userToCheck.getEmail();

        authenticationRepository.findByUsername(username)
                .ifPresent(u -> throwUserExists("username", username));

        authenticationRepository.findByEmail(email)
                .ifPresent(u -> throwUserExists("email", email));
    }

    public User authenticateUser(
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

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Wrong password for the user with username " + username
            );
        }

        if (!user.getEnabled()) {
            throw new DisabledException(
                    "Account for username " + username + " has not been verified yet"
            );
        }

        return user;
    }

    public User registerUser(
            User userToRegister
    ) {
        validateUserDoesNotExist(userToRegister);
        userToRegister.setPassword(passwordEncoder.encode(userToRegister.getPassword()));
        return authenticationRepository.save(userToRegister);
    }
}

package org.example.user.domain.services;


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
}

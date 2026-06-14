package org.example.user.application.login;

import org.example.user.domain.entities.User;
import org.example.user.domain.services.AuthenticationService;
import org.springframework.stereotype.Service;


@Service
public class LoginUseCase {
    private final AuthenticationService authenticationService;

    public LoginUseCase(
            AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }



    public String login(
            LoginRequest loginRequest
    ) {
        User user = authenticationService.getUserByUsername(
                loginRequest.username(),
                loginRequest.password()
        );

        return "User with username " + user.getUsername() + " Was logged in successfully";
    }
}

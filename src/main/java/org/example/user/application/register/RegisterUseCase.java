package org.example.user.application.register;


import org.example.user.domain.entities.User;
import org.example.user.domain.services.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegisterUseCase {
    private final AuthenticationService authenticationService;

    public RegisterUseCase(
            AuthenticationService authenticationService
    ) {
        this.authenticationService = authenticationService;
    }

    @Transactional
    public String registerUser(
            RegisterRequest registerRequest
    ) {
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(registerRequest.password());
        user.setBirthDate(registerRequest.birthDate());

        if (authenticationService.registerUser(user) != null) {
            return "User with username " + user.getUsername() + " Was created successfully!";
        }

        return "Something went wrong...";
    }
}

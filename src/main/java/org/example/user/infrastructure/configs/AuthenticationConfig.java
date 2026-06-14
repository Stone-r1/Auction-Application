package org.example.user.infrastructure.configs;

import org.example.user.domain.repositories.AuthenticationRepository;
import org.example.user.domain.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AuthenticationConfig {

    @Bean
    public AuthenticationService authenticationService(
            AuthenticationRepository authenticationRepository
    ) {
        return new AuthenticationService(authenticationRepository);
    }
}

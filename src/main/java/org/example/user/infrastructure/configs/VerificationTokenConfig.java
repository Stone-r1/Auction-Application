package org.example.user.infrastructure.configs;


import org.example.user.domain.repositories.AuthenticationRepository;
import org.example.shared.domain.EmailSenderRepository;
import org.example.user.domain.repositories.VerificationTokenRepository;
import org.example.user.domain.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class VerificationTokenConfig {

    @Bean
    public VerificationTokenService verificationTokenService(
            VerificationTokenRepository verificationTokenRepository,
            AuthenticationRepository authenticationRepository,
            EmailSenderRepository emailSenderRepository,
            @Value("${app.verification.base-url}") String verificationBaseUrl,
            @Value("${app.verification.token-ttl-hours:24}") Long tokenTimeToLive
    ) {
        return new VerificationTokenService(
                verificationTokenRepository,
                authenticationRepository,
                emailSenderRepository,
                verificationBaseUrl,
                Duration.ofHours(tokenTimeToLive)
        );
    }
}

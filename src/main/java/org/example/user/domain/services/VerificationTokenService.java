package org.example.user.domain.services;

import org.example.user.domain.entities.User;
import org.example.user.domain.entities.VerificationToken;
import org.example.user.domain.repositories.AuthenticationRepository;
import org.example.shared.domain.EmailSenderRepository;
import org.example.user.domain.repositories.VerificationTokenRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationRepository authenticationRepository;
    private final EmailSenderRepository emailSenderRepository;
    private final String verificationBaseUrl;
    private final Duration tokenTimeToLive;


    public VerificationTokenService(
            VerificationTokenRepository verificationTokenRepository,
            AuthenticationRepository authenticationRepository,
            EmailSenderRepository emailSenderRepository,
            String verificationBaseUrl,
            Duration tokenTimeToLive
    ) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.authenticationRepository = authenticationRepository;
        this.emailSenderRepository = emailSenderRepository;
        this.verificationBaseUrl = verificationBaseUrl;
        this.tokenTimeToLive = tokenTimeToLive;
    }

    public void sendVerificationEmail(
            User user
    ) {
        verificationTokenRepository.deleteByUser(user);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        verificationToken.setExpiresAt(Instant.now().plus(tokenTimeToLive));
        verificationTokenRepository.save(verificationToken);

        emailSenderRepository.send(
                user.getEmail(),
                "Verify Your Account",
                buildVerificationEmail(
                        verificationBaseUrl + "/verify?token=" + verificationToken.getToken()
                )
        );
    }

    public VerificationResult verify(
            String token
    ) {
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken(token);

        if (foundToken.isEmpty()) {
            return new VerificationResult.NotFound();
        }

        VerificationToken verificationToken = foundToken.get();
        User user = verificationToken.getUser();

        if (user.getEnabled()) {
            return new VerificationResult.AlreadyVerified(user);
        }

        if (verificationToken.isExpired()) {
            return new VerificationResult.Expired();
        }

        user.setEnabled(true);
        authenticationRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        return new VerificationResult.Success(user);
    }

    private String buildVerificationEmail(
            String verificationLink
    ) {
        return """
                <html>
                  <body style="font-family: sans-serif;">
                    <p>Thanks for registering. Click the button below to verify your account:</p>
                    <a href="%s" style="background:#4CAF50;color:#ffffff;padding:10px 20px;
                       text-decoration:none;border-radius:4px;display:inline-block;">Verify Email</a>
                    <p>This link expires in %d hours.</p>
                  </body>
                </html>
                """.formatted(verificationLink, tokenTimeToLive.toHours());
    }
}

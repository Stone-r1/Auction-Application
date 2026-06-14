package org.example.user.application.verification;


import org.example.user.domain.services.VerificationResult;
import org.example.user.domain.services.VerificationTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VerificationUseCase {
    private final VerificationTokenService verificationTokenService;

    public VerificationUseCase(
            VerificationTokenService verificationTokenService
    ) {
        this.verificationTokenService = verificationTokenService;
    }

    @Transactional
    public VerificationResult verify(
            String token
    ) {
        return verificationTokenService.verify(token);
    }
}

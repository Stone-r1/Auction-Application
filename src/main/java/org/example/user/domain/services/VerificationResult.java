package org.example.user.domain.services;


import org.example.user.domain.entities.User;


public sealed interface VerificationResult {
    record Success(User user) implements VerificationResult {}
    record AlreadyVerified(User user) implements VerificationResult {}
    record Expired() implements VerificationResult {}
    record NotFound() implements VerificationResult {}
}

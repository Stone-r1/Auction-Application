package org.example.user.domain.repositories;


import org.example.user.domain.entities.User;
import org.example.user.domain.entities.VerificationToken;

import java.util.Optional;


public interface VerificationTokenRepository {
    Optional<VerificationToken> findByToken(String token);
    VerificationToken save(VerificationToken token);
    void delete(VerificationToken token);
    void deleteByUser(User user);
}

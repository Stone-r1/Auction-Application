package org.example.user.infrastructure.adapters;

import org.example.user.domain.entities.User;
import org.example.user.domain.entities.VerificationToken;
import org.example.user.domain.repositories.VerificationTokenRepository;
import org.example.user.infrastructure.persistance.JpaVerificationTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class VerificationTokenRepositoryAdapter implements VerificationTokenRepository {
    private final JpaVerificationTokenRepository jpaVerificationTokenRepository;

    public VerificationTokenRepositoryAdapter(
            JpaVerificationTokenRepository jpaVerificationTokenRepository
    ) {
        this.jpaVerificationTokenRepository = jpaVerificationTokenRepository;
    }

    @Override
    public Optional<VerificationToken> findByToken(
            String token
    ) {
        return jpaVerificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken save(
            VerificationToken token
    ) {
        return jpaVerificationTokenRepository.save(token);
    }

    @Override
    public void delete(
            VerificationToken token
    ) {
        jpaVerificationTokenRepository.delete(token);
    }

    @Override
    public void deleteByUser(
            User user
    ) {
        jpaVerificationTokenRepository.deleteByUser(user);
    }
}

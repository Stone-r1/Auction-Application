package org.example.notification.infrastructure.adapters;


import org.example.notification.domain.repositories.NotificationRepository;
import org.example.user.infrastructure.persistance.JpaAuthenticationRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class NotificationAdapter implements NotificationRepository {
    private final JpaAuthenticationRepository jpaAuthenticationRepository;

    public NotificationAdapter(
            JpaAuthenticationRepository jpaAuthenticationRepository
    ) {
        this.jpaAuthenticationRepository = jpaAuthenticationRepository;
    }

    @Override
    public Optional<String> findEmailByUserId(
            Long userId
    ) {
        return jpaAuthenticationRepository.findEmailByUserId(userId);
    }
}

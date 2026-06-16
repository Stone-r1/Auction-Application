package org.example.notification.domain.repositories;

import java.util.Optional;


public interface NotificationRepository {
    Optional<String> findEmailByUserId(Long userId);
}

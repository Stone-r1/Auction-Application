package org.example.notification.infrastructure.configs;


import org.example.notification.domain.repositories.NotificationRepository;
import org.example.notification.domain.services.NotificationService;
import org.example.shared.domain.EmailSenderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NotificationConfig {

    @Bean
    public NotificationService notificationService(
            NotificationRepository notificationUserRepository,
            EmailSenderRepository emailSenderRepository
    ) {
        return new NotificationService(notificationUserRepository, emailSenderRepository);
    }
}

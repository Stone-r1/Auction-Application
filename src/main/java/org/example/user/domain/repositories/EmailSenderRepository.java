package org.example.user.domain.repositories;


public interface EmailSenderRepository {
    void send(String to, String subject, String htmlContents);
}

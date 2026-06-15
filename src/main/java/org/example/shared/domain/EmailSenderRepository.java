package org.example.shared.domain;


public interface EmailSenderRepository {
    void send(String to, String subject, String htmlContents);
}

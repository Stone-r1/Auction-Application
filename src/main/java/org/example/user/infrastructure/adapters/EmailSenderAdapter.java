package org.example.user.infrastructure.adapters;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.user.domain.repositories.EmailSenderRepository;
import org.example.user.infrastructure.exceptions.EmailDeliveryException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailSenderAdapter implements EmailSenderRepository {
    private final JavaMailSender mailSender;

    public EmailSenderAdapter(
            JavaMailSender javaMailSender
    ) {
        this.mailSender = javaMailSender;
    }

    @Override
    public void send(
            String to,
            String subject,
            String htmlContents
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom(subject);
            helper.setText(htmlContents, true);

            mailSender.send(message);

        } catch (MessagingException exception) {
            throw new EmailDeliveryException("Failed to send email to " + to, exception);
        }
    }
}

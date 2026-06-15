package org.example.shared.infrastructure.adapters;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.shared.domain.EmailSenderRepository;
import org.example.shared.infrastructure.exceptions.EmailDeliveryException;
import org.springframework.mail.MailException;
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
            helper.setSubject(subject);
            helper.setText(htmlContents, true);

            mailSender.send(message);

        } catch (MessagingException | MailException exception) {
            throw new EmailDeliveryException("Failed to send email to " + to, exception);
        }
    }
}

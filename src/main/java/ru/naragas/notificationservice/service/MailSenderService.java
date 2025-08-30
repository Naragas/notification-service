package ru.naragas.notificationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.naragas.notificationservice.exception.EmailDeliveryException;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
@Service
public class MailSenderService implements MailService{
    private final JavaMailSender javaMailSender;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.subjects.created}")
    private String createdSubject;

    @Value("${mail.subjects.deleted}")
    private String deletedSubject;

    @Value("${mail.texts.created}")
    private String createdText;

    @Value("${mail.texts.deleted}")
    private String deletedText;


    public MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendCreatedAccountEmail(String email) {
        SimpleMailMessage createMessage = getBaseMessage(email);
        createMessage.setSubject(createdSubject);
        createMessage.setText(createdText);
        try {
            javaMailSender.send(createMessage);
        } catch (MailException e) {
            throw new EmailDeliveryException(
              "Failed to send 'Account created' email.", e
            );
        }
    }

    @Override
    public void sendDeletedAccountEmail(String email) {
        SimpleMailMessage deleteMessage = getBaseMessage(email);
        deleteMessage.setSubject(deletedSubject);
        deleteMessage.setText(deletedText);
        try {
            javaMailSender.send(deleteMessage);
        } catch (MailException e) {
            throw new EmailDeliveryException(
                    "Failed to send 'Delete account' email.", e
            );
        }
    }

    private SimpleMailMessage getBaseMessage(String email) {
        SimpleMailMessage baseMessage = new SimpleMailMessage();
        baseMessage.setFrom(from);
        baseMessage.setTo(email);
        return baseMessage;
    }
}

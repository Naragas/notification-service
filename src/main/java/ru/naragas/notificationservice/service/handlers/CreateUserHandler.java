package ru.naragas.notificationservice.service.handlers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.naragas.notificationservice.dto.UserEventDTO;
import ru.naragas.notificationservice.dto.UserEventType;
import ru.naragas.notificationservice.service.MailSenderService;
import ru.naragas.notificationservice.service.MailService;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
@Service
public class CreateUserHandler implements UserEventHandler{
    private final MailService mailService;

    public CreateUserHandler(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public UserEventType getEventType() {
        return UserEventType.CREATE_USER;
    }

    @Override
    public void handle(UserEventDTO event) {
        mailService.sendCreatedAccountEmail(event.getEmail());
    }
}

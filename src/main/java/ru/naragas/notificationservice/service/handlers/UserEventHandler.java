package ru.naragas.notificationservice.service.handlers;


import ru.naragas.notificationservice.dto.UserEventDTO;
import ru.naragas.notificationservice.dto.UserEventType;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
public interface UserEventHandler {
    UserEventType getEventType();
    void handle(UserEventDTO event);
}

package ru.naragas.notificationservice.service;


import ru.naragas.notificationservice.dto.UserEventDTO;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
public interface MailService {
    void sendCreatedAccountEmail(String email);
    void sendDeletedAccountEmail(String email);
}

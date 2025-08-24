package ru.naragas.notificationservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.naragas.notificationservice.dto.UserEventDTO;
import ru.naragas.notificationservice.dto.UserEventType;
import ru.naragas.notificationservice.service.handlers.UserEventHandler;

import java.util.Map;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class NotificationController {
    private final Map<UserEventType, UserEventHandler> handlers;

    @PostMapping
    public ResponseEntity<?> sendNotification(@Valid @RequestBody UserEventDTO userEventDTO) {
        UserEventHandler handler = handlers.get(userEventDTO.getEventType());
        if (handler == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Invalid event type",
                            "eventType", userEventDTO.getEventType().name()
                    )
            );
        }

        handler.handle(userEventDTO);
        return ResponseEntity.ok().build();
    }
}

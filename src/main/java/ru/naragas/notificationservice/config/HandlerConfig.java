package ru.naragas.notificationservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.naragas.notificationservice.dto.UserEventType;
import ru.naragas.notificationservice.service.handlers.UserEventHandler;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */

@Configuration
public class HandlerConfig {

    @Bean
    public Map<UserEventType, UserEventHandler> handlers(List<UserEventHandler> handlers) {
        Map<UserEventType, UserEventHandler> handlerMap = new EnumMap<>(UserEventType.class);

        for(UserEventHandler handler : handlers) {
            handlerMap.put(handler.getEventType(), handler);
        }
        return handlerMap;
    }
}

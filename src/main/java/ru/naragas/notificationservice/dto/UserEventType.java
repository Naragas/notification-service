package ru.naragas.notificationservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
public enum UserEventType {
    @JsonProperty("CreateUser")
    CREATE_USER,
    @JsonProperty("DeleteUser")
    DELETE_USER
}

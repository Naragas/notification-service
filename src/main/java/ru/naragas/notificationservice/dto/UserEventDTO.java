package ru.naragas.notificationservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Naragas
 * @version 1.0
 * @created 8/24/2025
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
public class UserEventDTO {

    @NotNull
    private UserEventType eventType;

    @NotBlank
    @Email
    private String email;
}

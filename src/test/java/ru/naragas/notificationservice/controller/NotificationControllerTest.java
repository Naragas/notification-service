package ru.naragas.notificationservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.naragas.notificationservice.dto.UserEventDTO;
import ru.naragas.notificationservice.dto.UserEventType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Naragas
 * @version 1.0
 * @created 8/25/2025
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.kafka.listener.auto-startup=false")
@Import(NotificationControllerTest.MailSenderTestConfig.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavaMailSender javaMailSenderMock;

    @BeforeEach
    void resetMailSenderMock() {
        org.mockito.Mockito.clearInvocations(javaMailSenderMock);
    }

    @TestConfiguration
    static class MailSenderTestConfig {
        @Bean
        @Primary
        JavaMailSender javaMailSenderMock() {
            return org.mockito.Mockito.mock(JavaMailSender.class);
        }
    }

    @Test
    @DisplayName("POST /api/email - should send Create email")
    void shouldSendCreateMail() throws Exception {
        final String testEmail = "test@gmail.com";
        final String subject = "Account created";
        final String fromEmail = "noreply@naragas.ru";
        UserEventDTO userEventDTO = new UserEventDTO(UserEventType.CREATE_USER, testEmail);
        String requestJson = new ObjectMapper().writeValueAsString(userEventDTO);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSenderMock, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).contains(testEmail);
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getFrom()).isEqualTo(fromEmail);
    }

    @Test
    @DisplayName("POST /api/email - should send Delete email")
    void shouldSendDeleteMail() throws Exception {
        final String testEmail = "test@gmail.com";
        final String subject = "Account deleted";
        final String fromEmail = "noreply@naragas.ru";
        UserEventDTO userEventDTO = new UserEventDTO(UserEventType.DELETE_USER, testEmail);
        String requestJson = new ObjectMapper().writeValueAsString(userEventDTO);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSenderMock, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertThat(message.getTo()).contains(testEmail);
        assertThat(message.getSubject()).isEqualTo(subject);
        assertThat(message.getFrom()).isEqualTo(fromEmail);
    }

    @Test
    @DisplayName("POST /api/email - should return 400 if email is invalid")
    void shouldReturnBadRequestIfEmailIsInvalid() throws Exception {
        final String invalidEmail = "not-an-email";
        var dto = new UserEventDTO(UserEventType.CREATE_USER, invalidEmail);
        var json = new ObjectMapper().writeValueAsString(dto);

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        org.mockito.Mockito.verifyNoInteractions(javaMailSenderMock);
    }

    @Test
    @DisplayName("POST /api/email - should return 400 if event type is empty")
    void shouldReturnBadRequestIfEventTypeIsEmpty() throws Exception {
        var json = """
                {"eventType": null, "email": "test@gmail.com"}
                """;

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        org.mockito.Mockito.verifyNoInteractions(javaMailSenderMock);
    }

    @Test
    @DisplayName("POST /api/email - should return 400 if event type not valid")
    void shouldReturnBadRequestIfEventTypeIsInvalid() throws Exception {
        var json = """
        {"eventType": "UnknownEvent", "email": "test@gmail.com"}
        """;

        mockMvc.perform(post("/api/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        org.mockito.Mockito.verifyNoInteractions(javaMailSenderMock);
    }

}

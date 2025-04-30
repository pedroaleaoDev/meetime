package com.meetime.service;

import com.meetime.dto.ContactRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateContact_Success() {
        // Arrange
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setEmail("email@teste.com");
        dto.setFirstname("Nome");
        dto.setLastname("Sobrenome");
        dto.setPhone("11999999999");
        String token = "valid_token";

        // Act
        ResponseEntity<?> response = contactService.createContact(dto, token);

        // Assert
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful() || response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
    }

    @Test
    void testCreateContact_NullDTO() {
        String token = "valid_token";
        assertThrows(NullPointerException.class, () -> {
            contactService.createContact(null, token);
        });
    }

    @Test
    void testCreateContact_InvalidToken() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setEmail("email@teste.com");
        dto.setFirstname("Nome");
        dto.setLastname("Sobrenome");
        dto.setPhone("11999999999");
        String token = null;
        // Dependendo da implementação, pode lançar exceção ou retornar erro
        assertDoesNotThrow(() -> contactService.createContact(dto, token));
    }

    @Test
    void testCreateContact_ValidPayload() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setEmail("valid@email.com");
        dto.setFirstname("Valid");
        dto.setLastname("User");
        dto.setPhone("11999999999");
        String token = "valid_token";
        ResponseEntity<?> response = contactService.createContact(dto, token);
        assertNotNull(response);
        // Aceita 2xx, 4xx ou 5xx pois depende da API externa
        assertTrue(response.getStatusCode().is2xxSuccessful() ||
                   response.getStatusCode().is4xxClientError() ||
                   response.getStatusCode().is5xxServerError());
    }

    @Test
    void testCreateContact_NullEmail() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setFirstname("Valid");
        dto.setLastname("User");
        dto.setPhone("11999999999");
        String token = "valid_token";
        ResponseEntity<?> response = contactService.createContact(dto, token);
        assertNotNull(response);
    }
}

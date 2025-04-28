package com.meetime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.dto.ContactRequestDTO;
import com.meetime.dto.ContactsRequestDTO;
import com.meetime.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    private String bearerToken = "Bearer test_token";

    @Test
    void testAddContact_Success() throws Exception {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setEmail("email@teste.com");
        dto.setFirstname("Nome");
        dto.setLastname("Sobrenome");
        dto.setPhone("11999999999");

        when(contactService.createContact(any(ContactRequestDTO.class), anyString()))
                .thenReturn(ResponseEntity.status(201).body(dto));

        mockMvc.perform(post("/contacts/contact")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("email@teste.com"));
    }

    @Test
    void testAddContact_Unauthorized() throws Exception {
        ContactRequestDTO dto = new ContactRequestDTO();
        mockMvc.perform(post("/contacts/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token de autorização é obrigatório no formato: Bearer {access_token}"));
    }

    @Test
    void testCreateContacts_Success() throws Exception {
        ContactRequestDTO dto1 = new ContactRequestDTO();
        dto1.setEmail("email1@teste.com");
        dto1.setFirstname("Nome1");
        dto1.setLastname("Sobrenome1");
        dto1.setPhone("11999999999");
        ContactRequestDTO dto2 = new ContactRequestDTO();
        dto2.setEmail("email2@teste.com");
        dto2.setFirstname("Nome2");
        dto2.setLastname("Sobrenome2");
        dto2.setPhone("11888888888");
        ContactsRequestDTO contactsRequestDTO = new ContactsRequestDTO();
        contactsRequestDTO.setContacts(Arrays.asList(dto1, dto2));

        when(contactService.createContact(any(ContactRequestDTO.class), anyString()))
                .thenReturn(ResponseEntity.status(201).body(dto1))
                .thenReturn(ResponseEntity.status(201).body(dto2));

        mockMvc.perform(post("/contacts")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactsRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].email").value("email1@teste.com"))
                .andExpect(jsonPath("$[1].email").value("email2@teste.com"));
    }

    @Test
    void testCreateContacts_Unauthorized() throws Exception {
        ContactsRequestDTO contactsRequestDTO = new ContactsRequestDTO();
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactsRequestDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token de autorização é obrigatório no formato: Bearer {access_token}"));
    }

    @Test
    void testCreateContacts_EmptyList() throws Exception {
        ContactsRequestDTO contactsRequestDTO = new ContactsRequestDTO();
        contactsRequestDTO.setContacts(Collections.emptyList());
        mockMvc.perform(post("/contacts")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contactsRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("A lista de contatos não pode ser nula ou vazia."));
    }
}

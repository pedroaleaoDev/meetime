package com.meetime.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.dto.ContactRequestDTO;
import com.meetime.dto.ContactsRequestDTO;
import com.meetime.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
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
    void testCreateContacts_Unauthorized() throws Exception {
        // Testa quando não é informado o header Authorization
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contacts\": []}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Token de autorização é obrigatório no formato: Bearer {access_token}"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testCreateContacts_Success() throws Exception {
        // Prepara um DTO para teste
        ContactsRequestDTO dto = new ContactsRequestDTO();
        ContactRequestDTO contact = new ContactRequestDTO();
        contact.setEmail("test@email.com");
        contact.setFirstname("Test");
        contact.setLastname("User");
        contact.setPhone("11999999999");
        dto.getContacts().add(contact);
        
        // Resposta simulada do service
        ResponseEntity<String> mockResponse = ResponseEntity.status(201).body("Contato criado");
        
        // Mockando comportamento
        doReturn(mockResponse).when(contactService).createContact(any(), any());
        
        mockMvc.perform(post("/contacts")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Contato criado")));
    }

    @Test
    void testCreateContacts_BadRequest() throws Exception {
        // Testa com lista vazia de contatos
        ContactsRequestDTO dto = new ContactsRequestDTO();
        mockMvc.perform(post("/contacts")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("lista de contatos não pode ser nula ou vazia")));
    }
}

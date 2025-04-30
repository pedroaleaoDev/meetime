package com.meetime.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactRequestDTOTest {
    @Test
    void testGettersAndSetters() {
        ContactRequestDTO dto = new ContactRequestDTO();
        dto.setEmail("email@teste.com");
        dto.setFirstname("Nome");
        dto.setLastname("Sobrenome");
        dto.setPhone("11999999999");
        assertEquals("email@teste.com", dto.getEmail());
        assertEquals("Nome", dto.getFirstname());
        assertEquals("Sobrenome", dto.getLastname());
        assertEquals("11999999999", dto.getPhone());
    }
}

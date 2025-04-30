package com.meetime.dto;

import org.junit.jupiter.api.Test;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ContactsRequestDTOTest {
    @Test
    void testContactsListSetterGetter() {
        ContactsRequestDTO dto = new ContactsRequestDTO();
        assertNotNull(dto.getContacts());
        assertTrue(dto.getContacts().isEmpty());
        dto.setContacts(Collections.emptyList());
        assertNotNull(dto.getContacts());
        assertTrue(dto.getContacts().isEmpty());
    }
}

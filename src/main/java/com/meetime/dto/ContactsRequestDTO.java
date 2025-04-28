package com.meetime.dto;

import java.util.ArrayList;
import java.util.List;

public class ContactsRequestDTO {
    private List<ContactRequestDTO> contacts = new ArrayList<>();

    public List<ContactRequestDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactRequestDTO> contacts) {
        this.contacts = (contacts != null) ? contacts : new ArrayList<>();
    }
}
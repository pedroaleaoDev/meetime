package com.meetime.service;

import com.meetime.dto.ContactRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContactService {

    @Value("${hubspot.apiUrl}")
    private String hubspotApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> createContact(ContactRequestDTO dto, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        // Monta o payload no formato esperado pela HubSpot
        var properties = new HashMap<String, Object>();
        properties.put("email", dto.getEmail());
        properties.put("firstname", dto.getFirstname());
        properties.put("lastname", dto.getLastname());
        properties.put("phone", dto.getPhone());
        var body = new HashMap<String, Object>();
        body.put("properties", properties);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}

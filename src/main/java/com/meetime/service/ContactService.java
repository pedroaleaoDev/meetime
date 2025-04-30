package com.meetime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.meetime.dto.ContactRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContactService {

    @Value("${hubspot.apiUrl}")
    private String hubspotApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    // Deleta um contato pelo ID HubSpot
    public ResponseEntity<?> deleteContact(String id, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            return ResponseEntity.ok("Contato deletado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar contato: " + e.getMessage());
        }
    }

    // Lista propriedades customizadas do objeto contato
    public ResponseEntity<?> listContactProperties(String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/properties/contacts";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar propriedades: " + e.getMessage());
        }
    }

    // Lista engajamentos de um contato pelo ID HubSpot
    public ResponseEntity<?> listContactEngagements(String id, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts/" + id + "/associations/engagements";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar engajamentos: " + e.getMessage());
        }
    }

    // Buscar contato por ID
    public ResponseEntity<?> getContactById(String id, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar contato: " + e.getMessage());
        }
    }

    // Buscar contatos por filtro (search)
    public ResponseEntity<?> searchContacts(String searchBody, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(searchBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar contatos: " + e.getMessage());
        }
    }

    // Atualizar contato por ID
    public ResponseEntity<?> updateContact(String id, String updateBody, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(updateBody, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar contato: " + e.getMessage());
        }
    }

    // Cria um contato individual no HubSpot
    public ResponseEntity<?> createContact(ContactRequestDTO dto, String accessToken) {
        String url = hubspotApiUrl + "/crm/v3/objects/contacts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        var properties = new HashMap<String, Object>();
        properties.put("email", dto.getEmail());
        properties.put("firstname", dto.getFirstname());
        properties.put("lastname", dto.getLastname());
        properties.put("phone", dto.getPhone());
        var body = new HashMap<String, Object>();
        body.put("properties", properties);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            return restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar contato: " + e.getMessage());
        }
    }
}

package com.meetime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}

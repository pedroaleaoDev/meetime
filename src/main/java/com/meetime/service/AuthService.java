package com.meetime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${hubspot.clientId}")
    public String clientId;

    @Value("${hubspot.clientSecret}")
    private String clientSecret;

    @Value("${hubspot.redirectUri}")
    public String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String AUTHORIZATION_URL = "https://app.hubspot.com/oauth/authorize";
    private static final String TOKEN_URL = "https://api.hubapi.com/oauth/v1/token";
    private static final String SCOPES = "crm.schemas.deals.read oauth crm.objects.contacts.write crm.objects.companies.write crm.objects.companies.read crm.objects.deals.read crm.schemas.contacts.read crm.objects.deals.write crm.objects.contacts.read";

    public String generateAuthorizationUrl(String clientId, String redirectUri) {
        return AUTHORIZATION_URL +
                "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(SCOPES, StandardCharsets.UTF_8) +
                "&response_type=code";
    }

    public String exchangeAuthorizationCode(String code) {
        System.out.println("[DEBUG] Iniciando troca do código pelo token...");
        System.out.println("[DEBUG] Código recebido: " + code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        System.out.println("[DEBUG] Corpo da requisição preparado: " + body);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, request, String.class);

            System.out.println("[DEBUG] Resposta recebida. Status: " + response.getStatusCode());
            System.out.println("[DEBUG] Corpo da resposta: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                return "Token recebido com sucesso: " + response.getBody();
            } else {
                return "Erro ao obter token: " + response.getStatusCode() + " - " + response.getBody();
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Erro ao chamar a API de token do HubSpot: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao chamar a API de token do HubSpot", e);
        }
    }
}

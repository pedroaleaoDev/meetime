package com.meetime.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Armazena o último token obtido (em produção deveria ser persistido em banco de dados)
    private Map<String, Object> lastTokenData = new HashMap<>();

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

    public Map<String, Object> exchangeAuthorizationCode(String code) {
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
            
            if (response.getStatusCode().is2xxSuccessful()) {
                // Converte a resposta JSON para um Map
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                Map<String, Object> tokenData = objectMapper.convertValue(jsonNode, Map.class);
                
                // Armazena os dados do token (incluindo refresh_token)
                this.lastTokenData = tokenData;
                
                return tokenData;
            } else {
                throw new RuntimeException("Erro ao obter token: " + response.getStatusCode() + " - " + response.getBody());
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Erro ao chamar a API de token do HubSpot: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro ao chamar a API de token do HubSpot", e);
        }
    }
    
    public Map<String, Object> refreshAccessToken() {
        if (!lastTokenData.containsKey("refresh_token")) {
            throw new RuntimeException("Nenhum refresh token disponível. Faça a autorização completa primeiro.");
        }
        
        String refreshToken = (String) lastTokenData.get("refresh_token");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(TOKEN_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // Converte a resposta JSON para um Map
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                Map<String, Object> newTokenData = objectMapper.convertValue(jsonNode, Map.class);
                
                // Preserva o refresh token se ele não for retornado na resposta
                if (!newTokenData.containsKey("refresh_token") && lastTokenData.containsKey("refresh_token")) {
                    newTokenData.put("refresh_token", lastTokenData.get("refresh_token"));
                }
                
                // Atualiza os dados do token
                this.lastTokenData = newTokenData;
                
                return newTokenData;
            } else {
                throw new RuntimeException("Erro ao atualizar token: " + response.getStatusCode() + " - " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o token de acesso", e);
        }
    }
    
    public Map<String, Object> getValidToken() {
        if (lastTokenData.isEmpty()) {
            throw new RuntimeException("Nenhum token disponível. É necessário fazer a autorização inicial.");
        }
        
        // Se o token atual estiver prestes a expirar, atualiza-o
        if (isTokenExpired()) {
            return refreshAccessToken();
        }
        
        return lastTokenData;
    }
    
    private boolean isTokenExpired() {
        if (!lastTokenData.containsKey("expires_in") || !lastTokenData.containsKey("timestamp")) {
            return true; // Sem informações de expiração, assume que está expirado
        }
        
        long expiresIn = ((Number) lastTokenData.get("expires_in")).longValue();
        long timestamp = ((Number) lastTokenData.get("timestamp")).longValue();
        long currentTime = System.currentTimeMillis() / 1000;
        
        // Considera expirado se faltar menos de 5 minutos para expirar
        return (timestamp + expiresIn - currentTime) < 300;
    }
    
    public void setClientCredentials(String appId, String clientId, String clientSecret, String redirectUri) {
        // Permite configurar credenciais programaticamente
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}

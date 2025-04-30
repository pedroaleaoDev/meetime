package com.meetime.controller;

import com.meetime.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Gera a URL de autorização OAuth2 do HubSpot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "URL de autorização gerada com sucesso")
    })
    @GetMapping("/url")
    public ResponseEntity<String> getAuthorizationUrl() {
        // Passar os parâmetros clientId e redirectUri conforme o método do AuthService
        String url = authService.generateAuthorizationUrl(authService.clientId, authService.redirectUri);
        return ResponseEntity.ok(url);
    }

    @Operation(summary = "Callback OAuth2 do HubSpot, troca o code pelo access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token recebido com sucesso"),
        @ApiResponse(responseCode = "400", description = "Código inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno ao trocar o code pelo token")
    })
    @GetMapping("/callback")
    public ResponseEntity<?> oauthCallback(
            @Parameter(description = "Código de autorização retornado pelo HubSpot", required = true)
            @RequestParam("code") @NotBlank String code) {
        try {
            Map<String, Object> tokenData = authService.exchangeAuthorizationCode(code);
            return ResponseEntity.ok(tokenData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Erro ao trocar código pelo token", 
                             "message", e.getMessage()));
        }
    }
    
    @Operation(
        summary = "Obtém token de acesso OAuth2 através de fluxo simplificado",
        description = "Endpoint único que combina a geração da URL de autorização e obtenção do token. " +
                "Se o parâmetro 'code' não for fornecido, retorna uma resposta JSON contendo a URL de autorização " +
                "e instruções para o próximo passo. Se o 'code' for fornecido, troca este código pelo access token."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "URL de autorização ou token gerado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Código inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/token")
    public ResponseEntity<?> getAuthToken(
            @Parameter(description = "Código de autorização retornado pelo HubSpot após o redirect")
            @RequestParam(value = "code", required = false) String code,
            @Parameter(description = "ID do aplicativo HubSpot (opcional)")
            @RequestParam(value = "app_id", required = false) String appId,
            @Parameter(description = "Client ID do aplicativo HubSpot (opcional)")  
            @RequestParam(value = "client_id", required = false) String clientIdParam,
            @Parameter(description = "Client Secret do aplicativo HubSpot (opcional)")
            @RequestParam(value = "client_secret", required = false) String clientSecretParam) {
        
        // Configurar credenciais se fornecidas
        if (appId != null && clientIdParam != null && clientSecretParam != null) {
            authService.setClientCredentials(appId, clientIdParam, clientSecretParam, 
                authService.redirectUri);
        }
        
        if (code == null || code.isEmpty()) {
            // Sem código: retorna a URL de autorização e instruções
            String authUrl = authService.generateAuthorizationUrl(authService.clientId, authService.redirectUri);
            
            Map<String, Object> response = new HashMap<>();
            response.put("auth_url", authUrl);
            response.put("next_step", "Abra esta URL no navegador, authorize o acesso, " +
                    "depois copie o código 'code' da URL de redirecionamento e faça uma nova " +
                    "requisição para /auth/token?code=SEU_CÓDIGO_AQUI");
            
            if (appId != null) {
                response.put("app_id", appId);
                response.put("status", "Credenciais configuradas para este app");
            }
            
            return ResponseEntity.ok(response);
        } else {
            // Com código: troca pelo token
            try {
                Map<String, Object> tokenData = authService.exchangeAuthorizationCode(code);
                
                // Adiciona timestamp para controle de expiração
                if (!tokenData.containsKey("timestamp")) {
                    tokenData.put("timestamp", System.currentTimeMillis() / 1000);
                }
                
                Map<String, Object> response = new HashMap<>(tokenData);
                response.put("status", "Token obtido com sucesso");
                
                if (appId != null) {
                    response.put("app_id", appId);
                }
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Falha ao obter token");
                response.put("message", e.getMessage());
                
                if (appId != null) {
                    response.put("app_id", appId);
                }
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
    }
    
    @Operation(
        summary = "Obtém um token de acesso válido ou renova automaticamente",
        description = "Retorna um token de acesso válido para o aplicativo configurado. " +
                "Se o token atual estiver expirado, renova-o automaticamente usando o refresh token."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token válido obtido com sucesso"),
        @ApiResponse(responseCode = "400", description = "Nenhum token disponível, é necessário fazer a autorização inicial"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/valid-token")
    public ResponseEntity<?> getValidToken() {
        try {
            Map<String, Object> tokenData = authService.getValidToken();
            return ResponseEntity.ok(tokenData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Erro ao obter token válido", 
                             "message", e.getMessage(),
                             "solution", "Faça a autorização inicial usando /auth/token"));
        }
    }
}
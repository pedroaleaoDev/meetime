// AuthController.java
package com.meetime.controller;

import com.meetime.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // O método exchangeAuthorizationCode retorna String, mas o ideal é retornar ResponseEntity
        String tokenResponse = authService.exchangeAuthorizationCode(code);
        return ResponseEntity.ok(tokenResponse);
    }
}
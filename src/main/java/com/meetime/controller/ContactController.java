package com.meetime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    // Buscar contato por ID
    @Operation(
        summary = "Busca contato por ID no HubSpot",
        description = "Requer Bearer Token OAuth2. Retorna os dados de um contato pelo ID do HubSpot."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato encontrado"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getContactById(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return ResponseEntity.ok("Contato encontrado");
    }

    // Buscar contatos por filtro (ex: email, nome)
    @Operation(
        summary = "Busca contatos por filtro no HubSpot",
        description = "Requer Bearer Token OAuth2. Permite buscar contatos por filtros como email, nome, etc."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contatos encontrados"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/search")
    public ResponseEntity<?> searchContacts(
            @RequestBody(required = false) String searchBody,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return ResponseEntity.ok("Contatos encontrados");
    }
}

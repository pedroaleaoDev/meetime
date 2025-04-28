package com.meetime.controller;

import com.meetime.dto.ContactRequestDTO;
import com.meetime.dto.ContactsRequestDTO;
import com.meetime.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

    @Operation(
        summary = "Cria um contato real no HubSpot",
        description = "Requer Bearer Token OAuth2 para criar um contato real no HubSpot CRM. Corpo esperado:\n{\n  \"email\": \"exemplo@email.com\",\n  \"firstname\": \"João\",\n  \"lastname\": \"Silva\",\n  \"phone\": \"11999999999\"\n}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/contact")
    public ResponseEntity<?> addContact(
            @Valid @RequestBody ContactRequestDTO dto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Recebido DTO para criação de contato: {}", dto);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return contactService.createContact(dto, bearerToken);
    }

    @Operation(
        summary = "Cria múltiplos contatos no HubSpot",
        description = "Requer Bearer Token OAuth2 para criar vários contatos no HubSpot CRM. Corpo esperado:\n{\n  \"contacts\": [\n    {\n      \"email\": \"exemplo1@email.com\",\n      \"firstname\": \"João\",\n      \"lastname\": \"Silva\",\n      \"phone\": \"11999999999\"\n    },\n    {\n      \"email\": \"exemplo2@email.com\",\n      \"firstname\": \"Maria\",\n      \"lastname\": \"Oliveira\",\n      \"phone\": \"11888888888\"\n    }\n  ]\n}"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Contatos criados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<?> createContacts(
            @Valid @RequestBody ContactsRequestDTO dto,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("Recebido DTO para criação de múltiplos contatos: {}", dto);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        if (dto.getContacts() == null || dto.getContacts().isEmpty()) {
            return ResponseEntity.badRequest().body("A lista de contatos não pode ser nula ou vazia.");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        List<Object> results = new ArrayList<>();
        for (ContactRequestDTO contactDTO : dto.getContacts()) {
            logger.info("Enviando contato individual: {}", contactDTO);
            ResponseEntity<?> response = contactService.createContact(contactDTO, bearerToken);
            results.add(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }
}

package com.meetime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.meetime.service.ContactService;
import com.meetime.dto.ContactsRequestDTO;
import com.meetime.dto.ContactRequestDTO;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;

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
        return contactService.getContactById(id, bearerToken);
    }

    // Buscar contatos por filtro (ex: email, nome)
    @Operation(
        summary = "Busca contatos por filtro no HubSpot",
        description = "Requer Bearer Token OAuth2. Permite buscar contatos por filtros como email, nome, etc. O corpo da requisição deve seguir o formato de pesquisa da API HubSpot CRM v3.\n\n" +
                "Exemplo de corpo para buscar por email:\n" +
                "```json\n" +
                "{\n" +
                "  \"filterGroups\": [\n" +
                "    {\n" +
                "      \"filters\": [\n" +
                "        {\n" +
                "          \"propertyName\": \"email\",\n" +
                "          \"operator\": \"EQ\",\n" +
                "          \"value\": \"exemplo@email.com\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"sorts\": [\n" +
                "    {\n" +
                "      \"propertyName\": \"createdate\",\n" +
                "      \"direction\": \"DESCENDING\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"limit\": 10\n" +
                "}\n" +
                "```\n\n" +
                "Operadores disponíveis: EQ (igual), NEQ (diferente), LT (menor que), LTE (menor ou igual), GT (maior que), GTE (maior ou igual), BETWEEN, IN, NOT_IN, HAS_PROPERTY, NOT_HAS_PROPERTY, CONTAINS_TOKEN, NOT_CONTAINS_TOKEN."
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
        return contactService.searchContacts(searchBody, bearerToken);
    }

    // Atualizar contato por ID
    @Operation(
        summary = "Atualiza contato por ID no HubSpot",
        description = "Requer Bearer Token OAuth2. Atualiza os dados de um contato pelo ID do HubSpot."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato atualizado"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateContact(
            @PathVariable String id,
            @RequestBody String updateBody,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return contactService.updateContact(id, updateBody, bearerToken);
    }

    // Deleta um contato pelo ID HubSpot
    @Operation(
        summary = "Deleta um contato no HubSpot",
        description = "Requer Bearer Token OAuth2. Deleta um contato pelo ID do HubSpot."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato deletado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return contactService.deleteContact(id, bearerToken);
    }

    // Lista propriedades customizadas de contato
    @Operation(
        summary = "Lista propriedades do objeto contato no HubSpot",
        description = "Requer Bearer Token OAuth2. Retorna todas as propriedades do objeto contato."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Propriedades listadas com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/properties")
    public ResponseEntity<?> listContactProperties(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return contactService.listContactProperties(bearerToken);
    }

    // Lista engajamentos de um contato pelo ID HubSpot
    @Operation(
        summary = "Lista engajamentos de um contato no HubSpot",
        description = "Requer Bearer Token OAuth2. Retorna todos os engajamentos (tarefas, notas, reuniões, etc) associados ao contato pelo ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Engajamentos listados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "404", description = "Contato não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}/engagements")
    public ResponseEntity<?> listContactEngagements(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token de autorização é obrigatório no formato: Bearer {access_token}");
        }
        String bearerToken = authorizationHeader.replace("Bearer ", "").trim();
        return contactService.listContactEngagements(id, bearerToken);
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
            var result = new java.util.HashMap<String, Object>();
            result.put("contact", contactDTO);
            result.put("status", response.getStatusCode().value());
            result.put("body", response.getBody());
            results.add(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(results);
    }
}

package com.meetime.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de token OAuth2 recebida após o callback")
public class OAuthTokenResponseDTO {

    @Schema(description = "Tipo do token", example = "bearer")
    private String tokenType;

    @Schema(description = "Access token para autenticação", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "Token de refresh", example = "refreshToken-abc123")
    private String refreshToken;

    @Schema(description = "Tempo de expiração do access token em segundos", example = "1800")
    private int expiresIn;

    // Getters e Setters
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}

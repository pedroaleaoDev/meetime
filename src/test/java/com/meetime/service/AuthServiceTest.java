package com.meetime.service;

import com.meetime.dto.OAuthTokenResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExchangeAuthorizationCode_Success() {
        String code = "test_code";
        // O método real faz chamada HTTP, aqui só testamos se não lança exceção
        assertDoesNotThrow(() -> {
            String result = authService.exchangeAuthorizationCode(code);
        });
    }

    @Test
    void testExchangeAuthorizationCode_Error() {
        String code = null;
        assertThrows(Exception.class, () -> {
            authService.exchangeAuthorizationCode(code);
        });
    }
}

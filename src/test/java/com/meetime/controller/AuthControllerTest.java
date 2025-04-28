package com.meetime.controller;

import com.meetime.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void testAuthEndpoint() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testGetAuthorizationUrl() throws Exception {
        String expectedUrl = "https://app.hubspot.com/oauth/authorize?client_id=abc&redirect_uri=http://localhost:8080/callback&scope=scope&response_type=code";
        when(authService.generateAuthorizationUrl(anyString(), anyString())).thenReturn(expectedUrl);
        mockMvc.perform(get("/auth/url"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedUrl));
    }

    @Test
    void testCallbackEndpoint_Success() throws Exception {
        String code = "test_code";
        String expectedResponse = "Token recebido com sucesso!";
        when(authService.exchangeAuthorizationCode(anyString())).thenReturn(expectedResponse);

        mockMvc.perform(get("/auth/callback")
                .param("code", code))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testCallbackEndpoint_Error() throws Exception {
        String code = "test_code";
        when(authService.exchangeAuthorizationCode(anyString())).thenThrow(new RuntimeException("Erro ao trocar código pelo token"));

        mockMvc.perform(get("/auth/callback")
                .param("code", code))
                .andExpect(status().isInternalServerError());
    }
}

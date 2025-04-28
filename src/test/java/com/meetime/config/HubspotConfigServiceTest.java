package com.meetime.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

@SpringBootTest
@ActiveProfiles("test")
public class HubspotConfigServiceTest {
    private HubspotConfigService hubspotConfigService;
    private static final String TEST_CONFIG_PATH = "test-hubspot.config.yml";

    @BeforeEach
    public void setUp() throws Exception {
        // Crie um arquivo de configuração de teste
        String yaml = "defaultPortal: test-portal\n" +
                "portals:\n  - name: Test Portal\n    portalId: 123456\n    env: test\n    authType: oauth\n    auth:\n      tokenInfo:\n        accessToken: test-token\n        expiresAt: 999999999\n";
        FileWriter writer = new FileWriter(TEST_CONFIG_PATH);
        writer.write(yaml);
        writer.close();

        // Inicialize o serviço normalmente
        hubspotConfigService = new HubspotConfigService();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File(TEST_CONFIG_PATH);
        HubspotConfig config = mapper.readValue(configFile, HubspotConfig.class);
        // Use reflexão para setar o campo privado
        Field field = HubspotConfigService.class.getDeclaredField("hubspotConfig");
        field.setAccessible(true);
        field.set(hubspotConfigService, config);
    }

    @Test
    public void testGetConfigNotNull() {
        Assertions.assertNotNull(hubspotConfigService.getConfig());
    }

    @Test
    public void testGetDefaultPortal() {
        var portal = hubspotConfigService.getDefaultPortal();
        Assertions.assertNotNull(portal);
        Assertions.assertEquals("Test Portal", portal.getName());
        Assertions.assertEquals("123456", portal.getPortalId());
    }
}

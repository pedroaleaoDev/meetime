package com.meetime.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
public class HubspotConfigIntegrationTest {
    @Test
    public void testReadYamlAndMapToConfig() throws IOException {
        String yaml = "defaultPortal: test-portal\n" +
                "portals:\n  - name: Test Portal\n    portalId: 123456\n    env: test\n    authType: oauth\n    auth:\n      tokenInfo:\n        accessToken: test-token\n        expiresAt: 999999999\n";
        String filePath = "integration-hubspot.config.yml";
        FileWriter writer = new FileWriter(filePath);
        writer.write(yaml);
        writer.close();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        HubspotConfig config = mapper.readValue(new File(filePath), HubspotConfig.class);

        Assertions.assertEquals("test-portal", config.getDefaultPortal());
        Assertions.assertNotNull(config.getPortals());
        Assertions.assertEquals(1, config.getPortals().size());
        Assertions.assertEquals("Test Portal", config.getPortals().get(0).getName());
        Assertions.assertEquals("test-token", config.getPortals().get(0).getAuth().getTokenInfo().getAccessToken());
    }
}

package com.meetime.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.Optional;

@Service
public class HubspotConfigService {
    private HubspotConfig hubspotConfig;
    private static final String CONFIG_PATH = "hubspot.config.yml";

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            File configFile = new File(CONFIG_PATH);
            hubspotConfig = mapper.readValue(configFile, HubspotConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar o arquivo hubspot.config.yml", e);
        }
    }

    public HubspotConfig getConfig() {
        return hubspotConfig;
    }

    public HubspotConfig.Portal getDefaultPortal() {
        if (hubspotConfig == null || hubspotConfig.getPortals() == null) return null;
        return hubspotConfig.getPortals().stream()
                .filter(p -> p.getName().equals(hubspotConfig.getDefaultPortal()))
                .findFirst()
                .orElse(hubspotConfig.getPortals().isEmpty() ? null : hubspotConfig.getPortals().get(0));
    }

    public String getAccessToken() {
        HubspotConfig.Portal portal = getDefaultPortal();
        if (portal != null && portal.getAuth() != null && portal.getAuth().getTokenInfo() != null) {
            return portal.getAuth().getTokenInfo().getAccessToken();
        }
        return null;
    }

    public String getPortalId() {
        HubspotConfig.Portal portal = getDefaultPortal();
        return portal != null ? portal.getPortalId() : null;
    }

    public String getPersonalAccessKey() {
        HubspotConfig.Portal portal = getDefaultPortal();
        return portal != null ? portal.getPersonalAccessKey() : null;
    }
}

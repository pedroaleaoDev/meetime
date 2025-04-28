package com.meetime.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HubspotConfig {
    @JsonProperty("defaultPortal")
    private String defaultPortal;
    @JsonProperty("portals")
    private List<Portal> portals;

    public String getDefaultPortal() { return defaultPortal; }
    public void setDefaultPortal(String defaultPortal) { this.defaultPortal = defaultPortal; }
    public List<Portal> getPortals() { return portals; }
    public void setPortals(List<Portal> portals) { this.portals = portals; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Portal {
        private String name;
        private String portalId;
        private String env;
        private String authType;
        private Auth auth;
        private String accountType;
        private String personalAccessKey;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPortalId() { return portalId; }
        public void setPortalId(String portalId) { this.portalId = portalId; }
        public String getEnv() { return env; }
        public void setEnv(String env) { this.env = env; }
        public String getAuthType() { return authType; }
        public void setAuthType(String authType) { this.authType = authType; }
        public Auth getAuth() { return auth; }
        public void setAuth(Auth auth) { this.auth = auth; }
        public String getAccountType() { return accountType; }
        public void setAccountType(String accountType) { this.accountType = accountType; }
        public String getPersonalAccessKey() { return personalAccessKey; }
        public void setPersonalAccessKey(String personalAccessKey) { this.personalAccessKey = personalAccessKey; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Auth {
        private TokenInfo tokenInfo;

        public TokenInfo getTokenInfo() { return tokenInfo; }
        public void setTokenInfo(TokenInfo tokenInfo) { this.tokenInfo = tokenInfo; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenInfo {
        private String accessToken;
        private String expiresAt;

        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getExpiresAt() { return expiresAt; }
        public void setExpiresAt(String expiresAt) { this.expiresAt = expiresAt; }
    }
}

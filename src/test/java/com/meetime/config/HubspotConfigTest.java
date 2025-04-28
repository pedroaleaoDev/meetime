package com.meetime.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HubspotConfigTest {
    @Test
    public void testPortalSettersAndGetters() {
        HubspotConfig.Portal portal = new HubspotConfig.Portal();
        portal.setName("Test Portal");
        portal.setPortalId("123456");
        portal.setEnv("test");
        portal.setAuthType("oauth");
        portal.setAccountType("dev");
        portal.setPersonalAccessKey("key");

        Assertions.assertEquals("Test Portal", portal.getName());
        Assertions.assertEquals("123456", portal.getPortalId());
        Assertions.assertEquals("test", portal.getEnv());
        Assertions.assertEquals("oauth", portal.getAuthType());
        Assertions.assertEquals("dev", portal.getAccountType());
        Assertions.assertEquals("key", portal.getPersonalAccessKey());
    }

    @Test
    public void testAuthAndTokenInfo() {
        HubspotConfig.Auth auth = new HubspotConfig.Auth();
        HubspotConfig.TokenInfo tokenInfo = new HubspotConfig.TokenInfo();
        tokenInfo.setAccessToken("token");
        tokenInfo.setExpiresAt("999999999");
        auth.setTokenInfo(tokenInfo);

        Assertions.assertEquals("token", auth.getTokenInfo().getAccessToken());
        Assertions.assertEquals("999999999", auth.getTokenInfo().getExpiresAt());
    }
}

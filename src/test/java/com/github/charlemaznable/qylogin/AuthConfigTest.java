package com.github.charlemaznable.qylogin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class AuthConfigTest {

    @Autowired
    private AuthConfig authConfig;

    @Test
    public void testAuthConfig() {
        assertNotNull(authConfig);
        assertEquals("A916EFFC3121F935", authConfig.getEncryptKey());
        assertEquals("cookie-name", authConfig.getCookieName());
        assertEquals("redirect-uri", authConfig.getRedirectUri());
        assertEquals("local-url", authConfig.getLocalUrl());
        assertTrue(authConfig.isForceLogin());
    }
}

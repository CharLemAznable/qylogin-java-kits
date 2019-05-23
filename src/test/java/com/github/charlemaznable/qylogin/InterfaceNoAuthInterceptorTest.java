package com.github.charlemaznable.qylogin;

import com.github.charlemaznable.qylogin.config.AuthConfig;
import com.github.charlemaznable.qylogin.interceptor.AuthInterceptor;
import com.github.charlemaznable.qylogin.interfaceNoConfig.TestInterfaceNoConfiguration;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestInterfaceNoConfiguration.class)
public class InterfaceNoAuthInterceptorTest {

    @BeforeAll
    public static void beforeClass() {
        MockDiamondServer.setUpMockServer();
    }

    @AfterAll
    public static void afterClass() {
        MockDiamondServer.tearDownMockServer();
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AuthConfig authConfig;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Test
    public void testAuthConfig() {
        assertNotNull(authConfig);
        assertNull(authConfig.encryptKey());
        assertNull(authConfig.cookieName());
        assertNull(authConfig.redirectUri());
        assertNull(authConfig.localUrl());
        assertTrue(authConfig.forceLogin());
    }

    @SneakyThrows
    @Test
    public void testNoAuthInterceptor() {
        assertNotNull(authInterceptor);
        assertFalse(authInterceptor.preHandle(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(), null));
    }
}

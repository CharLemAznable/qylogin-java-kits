package com.github.charlemaznable.qylogin;

import com.github.charlemaznable.qylogin.config.AuthConfig;
import com.github.charlemaznable.qylogin.interceptor.AuthInterceptor;
import com.github.charlemaznable.qylogin.interfaceConfig.TestInterfaceConfiguration;
import lombok.SneakyThrows;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.charlemaznable.codec.Json.json;
import static org.joor.Reflect.on;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestInterfaceConfiguration.class)
public class InterfaceAuthInterceptorTest {

    @BeforeAll
    public static void beforeClass() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("QY_AUTH", "test",
                "encryptKey=A916EFFC3121F935\n" +
                        "cookieName=cookie-name\n" +
                        "redirectUri=redirect-uri\n" +
                        "localUrl=local-url");
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

    @SuppressWarnings("Duplicates")
    @Test
    public void testAuthConfig() {
        assertNotNull(authConfig);
        assertEquals("A916EFFC3121F935", authConfig.encryptKey());
        assertEquals("cookie-name", authConfig.cookieName());
        assertEquals("redirect-uri", authConfig.redirectUri());
        assertEquals("local-url", authConfig.localUrl());
        assertTrue(authConfig.forceLogin());
    }

    @SuppressWarnings("Duplicates")
    @SneakyThrows
    @Test
    public void testNoAuthInterceptor() {
        assertNotNull(authInterceptor);
        AuthConfig authConfig = on(authInterceptor).field("authConfig").get();
        assertNotNull(authConfig);
        assertEquals("A916EFFC3121F935", authConfig.encryptKey());
        assertEquals("cookie-name", authConfig.cookieName());
        assertEquals("redirect-uri", authConfig.redirectUri());
        assertEquals("local-url", authConfig.localUrl());
        assertTrue(authConfig.forceLogin());

        val cookieValue = new CookieValue();
        cookieValue.setUserId("a");
        cookieValue.setName("b");
        cookieValue.setAvatar("c");
        cookieValue.setCsrfToken("d");
        cookieValue.setRedirect("e");
        cookieValue.setExpired(DateTime.now().plusSeconds(3));
        val jsonString = json(cookieValue);
        val mockCookie = new MockCookie(authConfig.cookieName(), AES.encryptBase64(jsonString, authConfig.encryptKey()));
        val mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(mockCookie);
        val mockHttpServletResponse = new MockHttpServletResponse();
        val successResult = authInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null);
        assertTrue(successResult);
        assertNull(mockHttpServletResponse.getRedirectedUrl());

        Thread.sleep(5000);
        val failureResult = authInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, null);
        assertFalse(failureResult);
        assertEquals("redirect-uri?cookie=cookie-name&redirect=local-url", mockHttpServletResponse.getRedirectedUrl());
    }
}

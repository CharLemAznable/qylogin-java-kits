package com.github.charlemaznable.qylogin.defaultConfig;

import com.github.charlemaznable.qylogin.AES;
import com.github.charlemaznable.qylogin.CookieValue;
import lombok.SneakyThrows;
import lombok.val;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;

import static com.github.charlemaznable.core.codec.Json.json;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringJUnitWebConfig(DefaultConfiguration.class)
@TestInstance(Lifecycle.PER_CLASS)
public class DefaultConfigTest {

    private static MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeAll
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void testDefaultConfig() {
        val cookieValue = new CookieValue();
        cookieValue.setUserID("a");
        cookieValue.setName("b");
        cookieValue.setAvatar("c");
        cookieValue.setCsrfToken("d");
        cookieValue.setExpired(DateTime.now().plusSeconds(3));
        val jsonString = json(cookieValue);
        val mockCookie = new MockCookie("cookie-name", AES.encryptBase64(jsonString, "A916EFFC3121F935"));

        val verboseCookieValue = new CookieValue();
        verboseCookieValue.setUserID("a");
        verboseCookieValue.setName("b");
        verboseCookieValue.setAvatar("c");
        verboseCookieValue.setCsrfToken("d");
        verboseCookieValue.setExpired(DateTime.now());
        val verboseJsonString = json(verboseCookieValue);
        val verboseMockCookie = new MockCookie("verbose-cookie-name", AES.encryptBase64(verboseJsonString, "A916EFFC3121F935"));

        val response = mockMvc.perform(get("/default/index")
                .cookie(verboseMockCookie, mockCookie))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNull(response.getRedirectedUrl());

        assertDoesNotThrow(() ->
                await().pollDelay(Duration.ofMillis(5000)).until(() -> {
                    val response2 = mockMvc.perform(get("/default/index")
                            .cookie(verboseMockCookie, mockCookie))
                            .andExpect(status().isFound())
                            .andReturn().getResponse();
                    return "redirect-uri?cookie=cookie-name&redirect=local-url%2Fdefault%2Findex"
                            .equals(response2.getRedirectedUrl());
                }));
    }
}

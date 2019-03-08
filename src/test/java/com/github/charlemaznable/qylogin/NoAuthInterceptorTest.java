package com.github.charlemaznable.qylogin;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.joor.Reflect.on;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestNoConfiguration.class)
public class NoAuthInterceptorTest {

    @Autowired
    private AuthInterceptor authInterceptor;

    @SneakyThrows
    @Test
    public void testNoAuthInterceptor() {
        assertNotNull(authInterceptor);
        AuthConfig authConfig = on(authInterceptor).field("authConfig").get();
        assertNull(authConfig);
        assertFalse(authInterceptor.preHandle(
                new MockHttpServletRequest(),
                new MockHttpServletResponse(), null));
    }
}

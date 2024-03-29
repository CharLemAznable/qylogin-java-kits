package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.net.Url;
import com.github.charlemaznable.qylogin.CookieValue;
import com.github.charlemaznable.qylogin.QyLogin;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Str.isBlank;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static com.github.charlemaznable.qylogin.AES.decryptBase64;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

@Slf4j
public final class QyLoginInterceptor implements HandlerInterceptor {

    private final QyLoginConfig qyLoginConfig;
    private final Cache<HandlerQyLoginCacheKey, Optional<QyLogin>>
            handlerQyLoginCache = CacheBuilder.newBuilder().build();

    public QyLoginInterceptor(@Nullable QyLoginConfig qyLoginConfig) {
        this.qyLoginConfig = nullThen(qyLoginConfig, () -> getConfig(QyLoginConfig.class));
    }

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) throws Exception {
        if (null == qyLoginConfig) return false;
        if (!(handler instanceof HandlerMethod handlerMethod)) return true;

        val cacheKey = new HandlerQyLoginCacheKey(handlerMethod);
        val qyLoginOptional = handlerQyLoginCache.get(
                cacheKey, () -> findQyLogin(cacheKey));
        if (dontIntercept(qyLoginOptional)) return true;

        val encryptKey = qyLoginConfig.encryptKey();
        val cookieName = qyLoginConfig.cookieName();
        val redirectURI = qyLoginConfig.redirectURI();
        val localURL = qyLoginConfig.localURL();

        if (isBlank(encryptKey) || isBlank(cookieName) ||
                isBlank(redirectURI) || isBlank(localURL)) return false;

        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                val decrypted = decryptBase64(cookie.getValue(), encryptKey);
                val cookieValue = unJson(decrypted, CookieValue.class);
                if (cookieValue.getExpired().isBeforeNow() ||
                        isEmpty(cookieValue.getName())) break;
                return true;
            }
        }

        response.sendRedirect(redirectURI
                + (redirectURI.contains("?") ? "&" : "?")
                + "cookie=" + cookieName + "&"
                + "redirect=" + Url.encode(localURL + request.getRequestURI()));
        return false;
    }

    private Optional<QyLogin> findQyLogin(HandlerQyLoginCacheKey cacheKey) {
        val methodQyLogin = getMergedAnnotation(cacheKey.getMethod(), QyLogin.class);
        if (null != methodQyLogin) return Optional.of(methodQyLogin);

        val classQyLogin = getMergedAnnotation(cacheKey.getDeclaringClass(), QyLogin.class);
        if (null != classQyLogin) return Optional.of(classQyLogin);

        return Optional.empty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean dontIntercept(Optional<QyLogin> qyLoginOptional) {
        // +--------------------------+-------------------+--------------------+
        // |                          | forceLogin = true | forceLogin = false |
        // +--------------------------+-------------------+--------------------+
        // |    QyLogin not Present   |     intercept     |        pass        |
        // +--------------------------+-------------------+--------------------+
        // |  QyLogin required = true |     intercept     |      intercept     |
        // +--------------------------+-------------------+--------------------+
        // | QyLogin required = false |        pass       |        pass        |
        // +--------------------------+-------------------+--------------------+
        return (qyLoginOptional.isPresent() && !qyLoginOptional.get().required()) ||
                (qyLoginOptional.isEmpty() && !qyLoginConfig.forceLogin());
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerQyLoginCacheKey {

        private final Method method;
        private final Class<?> declaringClass;

        HandlerQyLoginCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }
}

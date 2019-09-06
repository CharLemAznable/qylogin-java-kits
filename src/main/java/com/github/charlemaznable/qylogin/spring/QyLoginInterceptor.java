package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.net.Url;
import com.github.charlemaznable.qylogin.CookieValue;
import com.github.charlemaznable.qylogin.QyLogin;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Str.isBlank;
import static com.github.charlemaznable.core.lang.Str.isEmpty;
import static com.github.charlemaznable.qylogin.AES.decryptBase64;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Slf4j
@Component
public class QyLoginInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private QyLoginConfig qyLoginConfig;
    private Cache<HandlerQyLoginCacheKey, Optional<QyLogin>>
            handlerQyLoginCache = CacheBuilder.newBuilder().build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;
        if (null == qyLoginConfig) return false;

        val handlerMethod = (HandlerMethod) handler;
        val cacheKey = new HandlerQyLoginCacheKey(handlerMethod);
        val qyLogin = handlerQyLoginCache.get(cacheKey, () -> findQyLogin(cacheKey));

        // +--------------------------+-------------------+--------------------+
        // |                          | forceLogin = true | forceLogin = false |
        // +--------------------------+-------------------+--------------------+
        // |    QyLogin not Present   |     intercept     |        pass        |
        // +--------------------------+-------------------+--------------------+
        // |  QyLogin required = true |     intercept     |      intercept     |
        // +--------------------------+-------------------+--------------------+
        // | QyLogin required = false |        pass       |        pass        |
        // +--------------------------+-------------------+--------------------+
        if (qyLogin.isPresent() && !qyLogin.get().required()) return true;
        if (!qyLogin.isPresent() && !qyLoginConfig.forceLogin()) return true;

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
                if (cookieValue.getExpired().isBeforeNow()) break;
                if (isEmpty(cookieValue.getName())) break;
                return true;
            }
        }

        var location = redirectURI + (redirectURI.contains("?") ? "&" : "?");
        location += "cookie=" + cookieName + "&";
        location += "redirect=" + Url.encode(localURL + request.getRequestURI());
        response.sendRedirect(location);
        return false;
    }

    private Optional<QyLogin> findQyLogin(HandlerQyLoginCacheKey cacheKey) {
        val methodQyLogin = findMergedAnnotation(cacheKey.getMethod(), QyLogin.class);
        if (null != methodQyLogin) return Optional.of(methodQyLogin);

        val classQyLogin = findMergedAnnotation(cacheKey.getDeclaringClass(), QyLogin.class);
        if (null != classQyLogin) return Optional.of(classQyLogin);

        return Optional.empty();
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerQyLoginCacheKey {

        private Method method;
        private Class<?> declaringClass;

        HandlerQyLoginCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }
}

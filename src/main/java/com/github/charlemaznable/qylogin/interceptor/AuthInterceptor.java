package com.github.charlemaznable.qylogin.interceptor;

import com.github.charlemaznable.net.Url;
import com.github.charlemaznable.qylogin.CookieValue;
import com.github.charlemaznable.qylogin.config.AuthConfig;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.github.charlemaznable.lang.Str.isEmpty;
import static com.github.charlemaznable.qylogin.AES.decryptBase64;

@NoArgsConstructor
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private AuthConfig authConfig;

    @Autowired(required = false)
    public AuthInterceptor(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == authConfig) return false;
        if (!authConfig.forceLogin()) return true;

        val cookieName = authConfig.cookieName();
        val encryptKey = authConfig.encryptKey();
        val redirectUri = authConfig.redirectUri();
        val localUrl = authConfig.localUrl();

        if (null == cookieName || null == encryptKey ||
                null == redirectUri || null == localUrl) return false;

        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                val decrypted = decryptBase64(cookie.getValue(), encryptKey);
                val cookieValue = unJson(decrypted, CookieValue.class);
                if (isEmpty(cookieValue.getName())) break;
                if (cookieValue.getExpired().isAfter(DateTime.now())) return true;
            }
        }

        var location = redirectUri + (redirectUri.contains("?") ? "&" : "?");
        location += "cookie=" + cookieName + "&";
        location += "redirect=" + Url.encode(localUrl + request.getRequestURI());
        response.sendRedirect(location);
        return false;
    }
}
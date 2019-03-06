package com.github.charlemaznable.qylogin;

import com.github.charlemaznable.net.Url;
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

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private AuthConfig authConfig;

    @Autowired
    public AuthInterceptor(AuthConfig authConfig) {
        this.authConfig = authConfig;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null == authConfig) return false;
        if (!authConfig.isForceLogin()) return true;

        val cookieName = authConfig.getCookieName();
        val encryptKey = authConfig.getEncryptKey();
        val redirectUri = authConfig.getRedirectUri();
        val localUrl = authConfig.getLocalUrl();

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

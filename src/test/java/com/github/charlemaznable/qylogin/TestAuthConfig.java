package com.github.charlemaznable.qylogin;

import org.springframework.stereotype.Component;

@Component
public class TestAuthConfig implements AuthConfig {

    @Override
    public String getEncryptKey() {
        return "A916EFFC3121F935";
    }

    @Override
    public String getCookieName() {
        return "cookie-name";
    }

    @Override
    public String getRedirectUri() {
        return "redirect-uri";
    }

    @Override
    public String getLocalUrl() {
        return "local-url";
    }

    @Override
    public boolean getForceLogin() {
        return true;
    }
}

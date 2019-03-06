package com.github.charlemaznable.qylogin;

public interface AuthConfig {

    String getEncryptKey();

    String getCookieName();

    String getRedirectUri();

    String getLocalUrl();

    boolean getForceLogin();
}

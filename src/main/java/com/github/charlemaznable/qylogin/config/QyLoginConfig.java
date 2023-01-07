package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.configservice.Config;

@Config(keyset = "QyLogin", key = "${qylogin-config:-default}")
public interface QyLoginConfig {

    @Config("EncryptKey")
    String encryptKey();

    @Config("CookieName")
    String cookieName();

    @Config("RedirectURI")
    String redirectURI();

    @Config("LocalURL")
    String localURL();

    @Config(key = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}

package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.configservice.apollo.ApolloConfig;
import com.github.charlemaznable.configservice.diamond.DiamondConfig;

@ApolloConfig(namespace = "QyLogin", propertyName = "${qylogin-config:-default}")
@DiamondConfig(group = "QyLogin", dataId = "${qylogin-config:-default}")
public interface QyLoginConfig {

    @ApolloConfig(propertyName = "EncryptKey")
    @DiamondConfig(dataId = "EncryptKey")
    String encryptKey();

    @ApolloConfig(propertyName = "CookieName")
    @DiamondConfig(dataId = "CookieName")
    String cookieName();

    @ApolloConfig(propertyName = "RedirectURI")
    @DiamondConfig(dataId = "RedirectURI")
    String redirectURI();

    @ApolloConfig(propertyName = "LocalURL")
    @DiamondConfig(dataId = "LocalURL")
    String localURL();

    @ApolloConfig(propertyName = "ForceLogin", defaultValue = "TRUE")
    @DiamondConfig(dataId = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}

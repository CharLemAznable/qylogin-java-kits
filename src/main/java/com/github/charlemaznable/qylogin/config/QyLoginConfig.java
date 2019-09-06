package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.core.miner.MinerConfig;

@MinerConfig(group = "QY_LOGIN", dataId = "default")
public interface QyLoginConfig {

    @MinerConfig(dataId = "EncryptKey")
    String encryptKey();

    @MinerConfig(dataId = "CookieName")
    String cookieName();

    @MinerConfig(dataId = "RedirectURI")
    String redirectURI();

    @MinerConfig(dataId = "LocalURL")
    String localURL();

    @MinerConfig(dataId = "ForceLogin", defaultValue = "TRUE")
    boolean forceLogin();
}

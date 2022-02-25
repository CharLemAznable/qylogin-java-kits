package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "QyLogin", dataId = "default")
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

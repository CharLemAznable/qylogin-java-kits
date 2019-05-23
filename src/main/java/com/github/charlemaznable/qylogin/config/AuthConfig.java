package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "QY_AUTH", dataId = "default")
public interface AuthConfig {

    String encryptKey();

    String cookieName();

    String redirectUri();

    String localUrl();

    @MinerConfig(defaultValue = "TRUE")
    boolean forceLogin();
}

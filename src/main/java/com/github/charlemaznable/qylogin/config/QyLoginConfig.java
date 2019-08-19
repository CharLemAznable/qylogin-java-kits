package com.github.charlemaznable.qylogin.config;

import com.github.charlemaznable.miner.MinerConfig;

@MinerConfig(group = "QY_LOGIN", dataId = "default")
public interface QyLoginConfig {

    String encryptKey();

    String cookieName();

    String redirectUri();

    String localUrl();

    @MinerConfig(defaultValue = "TRUE")
    boolean forceLogin();
}

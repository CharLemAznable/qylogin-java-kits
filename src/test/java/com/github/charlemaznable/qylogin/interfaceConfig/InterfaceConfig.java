package com.github.charlemaznable.qylogin.interfaceConfig;

import com.github.charlemaznable.miner.MinerConfig;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;

@MinerConfig(group = "QyLogin", dataId = "test")
public interface InterfaceConfig extends QyLoginConfig {

    @MinerConfig(dataId = "InterfaceCookieName")
    String cookieName();
}

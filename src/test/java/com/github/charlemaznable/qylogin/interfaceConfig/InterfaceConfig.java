package com.github.charlemaznable.qylogin.interfaceConfig;

import com.github.charlemaznable.configservice.diamond.DiamondConfig;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;

@DiamondConfig(group = "QyLogin", dataId = "test")
public interface InterfaceConfig extends QyLoginConfig {

    @DiamondConfig(dataId = "InterfaceCookieName")
    String cookieName();
}

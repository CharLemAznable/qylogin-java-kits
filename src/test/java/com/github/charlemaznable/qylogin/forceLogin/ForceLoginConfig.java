package com.github.charlemaznable.qylogin.forceLogin;

import com.github.charlemaznable.configservice.diamond.DiamondConfig;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;

@DiamondConfig(group = "QyLogin", dataId = "forceLogin")
public interface ForceLoginConfig extends QyLoginConfig {
}

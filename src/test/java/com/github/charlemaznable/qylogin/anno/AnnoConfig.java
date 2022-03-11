package com.github.charlemaznable.qylogin.anno;

import com.github.charlemaznable.configservice.diamond.DiamondConfig;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;

@DiamondConfig(group = "QyLogin", dataId = "anno")
public interface AnnoConfig extends QyLoginConfig {
}

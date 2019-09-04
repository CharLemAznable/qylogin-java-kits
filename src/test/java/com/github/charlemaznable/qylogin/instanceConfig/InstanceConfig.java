package com.github.charlemaznable.qylogin.instanceConfig;

import com.github.charlemaznable.qylogin.config.QyLoginConfig;
import org.springframework.stereotype.Component;

@Component
public class InstanceConfig implements QyLoginConfig {

    @Override
    public String encryptKey() {
        return "A916EFFC3121F935";
    }

    @Override
    public String cookieName() {
        return "cookie-name";
    }

    @Override
    public String redirectURI() {
        return "redirect-uri?v=verbose";
    }

    @Override
    public String localURL() {
        return "local-url";
    }

    @Override
    public boolean forceLogin() {
        return true;
    }
}

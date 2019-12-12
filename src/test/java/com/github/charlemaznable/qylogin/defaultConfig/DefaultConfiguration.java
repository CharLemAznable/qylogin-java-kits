package com.github.charlemaznable.qylogin.defaultConfig;

import com.github.charlemaznable.core.miner.MinerFactory;
import com.github.charlemaznable.qylogin.spring.QyLoginImport;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.joor.Reflect.onClass;

@EnableWebMvc
@ComponentScan
@QyLoginImport
public class DefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        onClass(MinerFactory.class).field("minerCache").call("invalidateAll");
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("QyLogin", "default",
                "EncryptKey=A916EFFC3121F935\n" +
                        "CookieName=cookie-name\n" +
                        "RedirectURI=redirect-uri\n" +
                        "LocalURL=local-url");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}

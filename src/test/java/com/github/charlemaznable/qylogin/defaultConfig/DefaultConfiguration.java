package com.github.charlemaznable.qylogin.defaultConfig;

import com.github.charlemaznable.miner.MinerScan;
import com.github.charlemaznable.qylogin.spring.QyLoginImport;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@EnableWebMvc
@ComponentScan
@QyLoginImport
@MinerScan("com.github.charlemaznable.qylogin.config")
public class DefaultConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("QY_LOGIN", "default",
                "encryptKey=A916EFFC3121F935\n" +
                        "cookieName=cookie-name\n" +
                        "redirectUri=redirect-uri\n" +
                        "localUrl=local-url");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}
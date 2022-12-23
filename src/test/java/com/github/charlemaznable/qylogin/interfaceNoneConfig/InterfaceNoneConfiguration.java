package com.github.charlemaznable.qylogin.interfaceNoneConfig;

import com.github.charlemaznable.configservice.diamond.DiamondScan;
import com.github.charlemaznable.qylogin.spring.QyLoginImport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.n3r.diamond.client.impl.MockDiamondServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@QyLoginImport
@DiamondScan
public class InterfaceNoneConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}

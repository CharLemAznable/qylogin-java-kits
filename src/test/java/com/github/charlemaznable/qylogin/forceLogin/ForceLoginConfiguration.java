package com.github.charlemaznable.qylogin.forceLogin;

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
public class ForceLoginConfiguration {

    @PostConstruct
    public void postConstruct() {
        MockDiamondServer.setUpMockServer();
        MockDiamondServer.setConfigInfo("QyLogin", "forceLogin", """
                EncryptKey=A916EFFC3121F935
                CookieName=cookie-name
                RedirectURI=redirect-uri
                LocalURL=local-url
                ForceLogin=false""");
    }

    @PreDestroy
    public void preDestroy() {
        MockDiamondServer.tearDownMockServer();
    }
}

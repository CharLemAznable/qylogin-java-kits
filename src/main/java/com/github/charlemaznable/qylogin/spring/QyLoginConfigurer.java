package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.spring.SpringContext.getBeanOrAutowire;

@Configuration
@ElvesImport
public class QyLoginConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(qyLoginInterceptor());
    }

    @Bean("com.github.charlemaznable.qylogin.spring.QyLoginInterceptor")
    public QyLoginInterceptor qyLoginInterceptor() {
        return new QyLoginInterceptor(qyLoginConfig());
    }

    private QyLoginConfig qyLoginConfig() {
        return getBeanOrAutowire(QyLoginConfig.class, defaultQyLoginConfigSupplier());
    }

    private Supplier<QyLoginConfig> defaultQyLoginConfigSupplier() {
        return () -> getConfig(QyLoginConfig.class);
    }
}

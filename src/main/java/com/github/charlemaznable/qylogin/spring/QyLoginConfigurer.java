package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.qylogin.config.QyLoginConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

import static com.github.charlemaznable.configservice.ConfigFactory.getConfig;
import static com.github.charlemaznable.core.lang.Condition.nullThen;

@Configuration
@ElvesImport
public class QyLoginConfigurer implements WebMvcConfigurer {

    private final QyLoginConfig qyLoginConfig;

    @Autowired
    public QyLoginConfigurer(@Nullable QyLoginConfig qyLoginConfig) {
        this.qyLoginConfig = nullThen(qyLoginConfig, () -> getConfig(QyLoginConfig.class));
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(qyLoginInterceptor());
    }

    @Bean("com.github.charlemaznable.qylogin.spring.QyLoginInterceptor")
    public QyLoginInterceptor qyLoginInterceptor() {
        return new QyLoginInterceptor(qyLoginConfig);
    }
}

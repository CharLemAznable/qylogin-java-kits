package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.spring.ElvesImport;
import com.github.charlemaznable.core.spring.NeoComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ElvesImport
@NeoComponentScan
public class QyLoginConfigurer implements WebMvcConfigurer {

    private final QyLoginInterceptor qyLoginInterceptor;

    @Autowired
    public QyLoginConfigurer(QyLoginInterceptor qyLoginInterceptor) {
        this.qyLoginInterceptor = qyLoginInterceptor;
    }

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(qyLoginInterceptor);
    }
}

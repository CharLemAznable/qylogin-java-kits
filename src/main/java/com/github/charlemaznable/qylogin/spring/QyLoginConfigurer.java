package com.github.charlemaznable.qylogin.spring;

import com.github.charlemaznable.core.spring.ComplexComponentScan;
import com.github.charlemaznable.core.spring.ComplexImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@SuppressWarnings("SpringFacetCodeInspection")
@Configuration
@ComplexImport
@ComplexComponentScan
public class QyLoginConfigurer implements WebMvcConfigurer {

    @Autowired
    private QyLoginInterceptor qyLoginInterceptor;

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(qyLoginInterceptor);
    }
}

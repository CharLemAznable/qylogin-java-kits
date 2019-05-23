package com.github.charlemaznable.qylogin.instanceConfig;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan(
        basePackages = {
                "com.github.charlemaznable.qylogin.interceptor",
                "com.github.charlemaznable.qylogin.instanceConfig"
        }
)
public class TestInstanceConfiguration {
}

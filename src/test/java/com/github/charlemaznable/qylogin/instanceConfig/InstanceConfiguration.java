package com.github.charlemaznable.qylogin.instanceConfig;

import com.github.charlemaznable.qylogin.spring.QyLoginImport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@QyLoginImport
public class InstanceConfiguration {
}

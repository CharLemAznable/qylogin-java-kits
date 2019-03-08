package com.github.charlemaznable.qylogin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;

import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

@ComponentScan(excludeFilters = @Filter(value = TestAuthConfig.class, type = ASSIGNABLE_TYPE))
public class TestNoConfiguration {
}

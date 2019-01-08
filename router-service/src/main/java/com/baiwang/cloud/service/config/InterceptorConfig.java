package com.baiwang.cloud.service.config;


import com.baiwang.cloud.service.interceptor.TestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

    @Bean
    public TestInterceptor testInterceptor(){
        return new TestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor()).addPathPatterns("/**");//配置拦截路径
        super.addInterceptors(registry);
    }

}

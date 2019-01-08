package com.baiwang.cloud.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

/**
 * ClassName: StartupApplication <br/>
 * Function: springboot启动类，自动加载启动配置,注册服务. <br/>
 * date: 2017年11月7日 下午2:52:59 <br/>
 *
 * @author wuyuegang
 * @SpringBootApplication 服务启动注解<br>
 * @EnableEurekaClient EnableEurekaClient注解将本系统所有controller的服务注册到Eureka注册中心<br>
 * 注册地址通过启动配置项进行设置 eureka.client.serviceUrl.defaultZone<br/>
 * 服务注册名通过启动配置项进行设置 spring.application.name=service-router-demo
 * @ComponentScan 注解会自动扫描子工程下的Bean, 包括引入的jar包
 * @since JDK 1.8
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@ComponentScan(basePackages = {"com.baiwang.cloud"})
@EnableFeignClients(basePackages = {"com.baiwang.cloud.service","com.baiwang.cloud.integration"})
public class StartupApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartupApplication.class, args);
    }

}

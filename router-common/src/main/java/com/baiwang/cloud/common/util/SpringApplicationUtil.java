package com.baiwang.cloud.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 英语
 */
@Component
public class SpringApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringApplicationUtil.context = context;
    }


    public static Object getBeanByName(String beanName) {
        return context == null ? null : context.getBean(beanName);
    }

    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

    public static <T>  T getBeanByType(Class<T> type) {
        return context == null ? null : context.getBean(type);
    }
}

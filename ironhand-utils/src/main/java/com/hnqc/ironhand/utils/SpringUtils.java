package com.hnqc.ironhand.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware, DisposableBean {
    public static ApplicationContext applicationContext = null;

    public static <T> T getBean(String beanName) {
        try {
            return (T) applicationContext.getBean(beanName);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void destroy() throws Exception {
        SpringUtils.applicationContext = null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

}
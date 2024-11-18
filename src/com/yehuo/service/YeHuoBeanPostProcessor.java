package com.yehuo.service;

import com.yehuo.spring.BeanPostProcessor;
import com.yehuo.spring.Component;

@Component
public class YeHuoBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")) {
            System.out.println("aaa");
        }
    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")) {
            System.out.println("bbb");
        }
    }
}

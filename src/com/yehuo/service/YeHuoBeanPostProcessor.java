package com.yehuo.service;

import com.yehuo.spring.BeanPostProcessor;
import com.yehuo.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class YeHuoBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")) {
            System.out.println("aaa");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")) {
            Object instance = Proxy.newProxyInstance(
                    YeHuoBeanPostProcessor.class.getClassLoader(),
                    bean.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("切面逻辑");
                            // 执行 bean对象的方法, 应该是接口方法
                            return method.invoke(bean, args);
                        }
                    }
            );
            return instance;
        }
        return bean;
    }
}

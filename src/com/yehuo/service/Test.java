package com.yehuo.service;

import com.yehuo.spring.YeHuoApplicationContext;

public class Test {
    public static void main(String[] args) {
        YeHuoApplicationContext context = new YeHuoApplicationContext(AppConfig.class);
        // context.getBean("userService") 拿到的是UserInterface类型的对象, createBean返回的是代理对象(UserInterface类型的)，
        UserInterface userService = (UserInterface) context.getBean("userService");
        userService.test();
    }
}

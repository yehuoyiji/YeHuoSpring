package com.yehuo.service;

import com.yehuo.spring.YeHuoApplicationContext;

public class Test {
    public static void main(String[] args) {
        YeHuoApplicationContext context = new YeHuoApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("userService");
    }
}

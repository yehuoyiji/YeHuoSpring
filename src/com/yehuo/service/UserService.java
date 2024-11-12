package com.yehuo.service;

import com.yehuo.spring.Autowired;
import com.yehuo.spring.Component;

@Component(beanName = "userService")
public class UserService {

    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println(orderService);
    }
}

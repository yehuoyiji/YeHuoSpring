package com.yehuo.service;

import com.yehuo.spring.Autowired;
import com.yehuo.spring.BeanNameAware;
import com.yehuo.spring.Component;

@Component(beanName = "userService")
public class UserService implements BeanNameAware {

    @Autowired
    private OrderService orderService;

    private String beanName;
    public void test() {
        System.out.println(orderService);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

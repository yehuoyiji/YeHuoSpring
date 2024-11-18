package com.yehuo.service;

import com.yehuo.spring.Autowired;
import com.yehuo.spring.BeanNameAware;
import com.yehuo.spring.Component;
import com.yehuo.spring.InitializingBean;

@Component(beanName = "userService")
public class UserService implements BeanNameAware, InitializingBean, UserInterface {

    @Autowired
    private OrderService orderService;

    private String beanName;
    public void test() {
        System.out.println("123123");
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    // 初始化方法
    public void afterPropertiesSet() {

    }
}

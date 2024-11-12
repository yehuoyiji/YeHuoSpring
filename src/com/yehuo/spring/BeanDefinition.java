package com.yehuo.spring;

public class BeanDefinition {

    // 类型
    private Class type;
    // 单例还是多例
    private String scope;

    public void setType(Class type) {
        this.type = type;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}

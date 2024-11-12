package com.yehuo.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 注解生效时间
@Retention(RetentionPolicy.RUNTIME)
// 只能写在类上面
@Target(ElementType.TYPE)
public @interface ComponentScan {

    // 扫描路径
    String value() default "";
}

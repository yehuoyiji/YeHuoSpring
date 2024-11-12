package com.yehuo.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class YeHuoApplicationContext {

    // 用于接收容器创建时的配置类
    private Class configClass;

    private ConcurrentHashMap<String, BeanDefinition> BeanDefinitionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public YeHuoApplicationContext(Class configClass) {
        this.configClass = configClass;


        // 扫描配置类的路径，判断是否有componentscan。 扫描 ---> BeanDefinition ---> BeanDefinitionMap
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            // 拿到扫描路径
            String path = componentScanAnnotation.value(); // com.yehuo.service

            path = path.replace(".", "/"); // com/yehuo/service
            ClassLoader classLoader = YeHuoApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile()); // D:\YeHuoSpring\out\production\YeHuoSpring\com\yehuo\service

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    // 拿到类名后，需要判断这个类是否是一个bean, 就看他有没有Component注解
                    String fileName = f.getAbsolutePath(); //D:\YeHuoSpring\out\production\YeHuoSpring\com\yehuo\service\UserService.class
                    if (fileName.endsWith(".class")) {
                        // loadClass 需要加载的是 xxx.xxx.xxx格式的路径，索引需要进行转换
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("\\", ".");
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            // 判断它是否有component 注解,即是否是一个bean
                            if (clazz.isAnnotationPresent(Component.class)) {
                                // BeanDefinition
                                Component component = clazz.getAnnotation(Component.class);
                                // bean的名字
                                String beanName = component.beanName();
                                if (beanName.equals("")) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)){
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    String scope = scopeAnnotation.value();
                                    beanDefinition.setScope(scope);
                                }else {
                                    beanDefinition.setScope("singleton");
                                }
                                BeanDefinitionMap.put(beanName, beanDefinition);


                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }

                }
            }
        }

        // 创建单例bean
        for (String beanName: BeanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = BeanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    // 创建bean
    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            // Bean 生命周期第一步:初始化，通过构造方法创建
            Object o = clazz.getConstructor().newInstance();
            // Bean 生命周期第二步:属性赋值
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(Autowired.class)) {
                    // 这样才能赋值
                    f.setAccessible(true);
                    // 从userService里的字段找到Autowire后，拿到Autowire注解下面的字段名去单例池拿到对应的bean后进行赋值
                    f.set(o, getBean(f.getName()));
                }
            }
            return o;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = BeanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new RuntimeException("不存在名称为:" + beanName + "的bean");
        }else {
            String scope = beanDefinition.getScope();
            if ("singleton".equals(scope)) {
                Object bean = singletonObjects.get(beanName);
                if (bean == null) {
                    Object o = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, o);
                }
                return bean;
                // 单例
            }else {
                return createBean(beanName, beanDefinition);
            }
        }
    }
}

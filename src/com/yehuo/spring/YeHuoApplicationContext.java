package com.yehuo.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class YeHuoApplicationContext {

    // 用于接收容器创建时的配置类
    private Class configClass;

    private ConcurrentHashMap<String, BeanDefinition> BeanDefinitionMap = new ConcurrentHashMap<>();

    public YeHuoApplicationContext(Class configClass) {
        this.configClass = configClass;


        // 扫描配置类的路径，判断是否有componentscan
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
                                String beanName = component.value();
                                System.out.println(beanName);
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
    }

    public Object getBean(String beanName) {
        return null;
    }
}

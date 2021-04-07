package com.yc.biz;

import com.yc.AppConfig;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class HelloWorldTestTest extends TestCase {
    private ApplicationContext ac;

    @Override
    @Before
    public void setUp() throws Exception {
        // AnnotationConfigApplicationContext 基于注解的容器配置类
        ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //读取 AppConfig.class ->basePackages  ="com.yc" ->得到要扫描的路径
        // 要检查这些包中的上是否有@Component 注解如有，则实例化
        //存到一个Map<String,Object>  ===ac
        //
      }


    public void testHello(){
        HelloWorld hw =(HelloWorld) ac.getBean("helloWorld");
        hw.hello();


      }

}
package com.yc;

import com.yc.bean.HelloWorld;
import com.yc.biz.StudentBiz;
import com.yc.springframework.context.MyAnnotationConfigApplicationContext;
import com.yc.springframework.context.MyApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Test {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {
        //得到IOC容器
        //将管理Bean的AppConfig.class传入MyAnnotationConfigApplicationContext构造方法
        MyApplicationContext ac = new MyAnnotationConfigApplicationContext(MyAppConfig.class);
        HelloWorld hw = (HelloWorld) ac.getBean("hw");
        hw.show();
        StudentBiz biz =(StudentBiz)ac.getBean("studentBiz");
        biz.add("shuaiqi");
    }
}
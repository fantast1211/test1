package com.yc.bean;

import com.yc.springframework.steretype.MyPostConstruct;
import com.yc.springframework.steretype.MyPreDestroy;

public class HelloWorld {

    @MyPostConstruct
    public void setup(){
        System.out.println("MyPostConstrust");
    }

    @MyPreDestroy
    public void destroy(){
        System.out.println("MyPreDestroy");
    }

    public HelloWorld(){
        System.out.println("hello world 构造");
    }

    public  void show (){
        System.out.println("show");
    }
}

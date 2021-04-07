package com.yc;

import com.yc.bean.HelloWorld;
import com.yc.springframework.steretype.MyBean;
import com.yc.springframework.steretype.MyComponentScan;
import com.yc.springframework.steretype.MyConfiguration;

@MyConfiguration//表示当前类是一个配置类
@MyComponentScan(basePackages = {"com.yc.dao","com.yc.biz"})//表示该类要扫描的范围
public class MyAppConfig {

    @MyBean
    public HelloWorld hw(){
        return  new HelloWorld();
    }

    public HelloWorld hw2(){
        return  new HelloWorld();
    }

}

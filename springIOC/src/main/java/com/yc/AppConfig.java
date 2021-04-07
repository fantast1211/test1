package com.yc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration//表示当前表示的类是一个配置类
@ComponentScan(basePackages = "com.yc")//将来压迫托管的bean要扫描的包以及子包
public class AppConfig {

}

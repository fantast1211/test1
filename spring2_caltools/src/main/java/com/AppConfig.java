package com;

import com.huwei.bean.Container;
import com.mimi.bean.Person;
import com.mimi.bean.PersonBmiTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Random;


@Configuration
@ComponentScan(basePackages = {"com.huwei","com.mimi"})
public class AppConfig {
    @Bean
    public Random r(){
        return  new Random();
    }
    @Bean
    public Person p8(){
        return new Person( "王八", 1.60, 90);
    }

}

package com.yc.dao;

import com.yc.springframework.steretype.MyRepository;

//@MyRepository  //异常转换: 从exception 转为 Runtime exception注解
public class StudentDaoMybatis implements StudentDao{
    @Override
    public int add(String name) {
        System.out.println("Mabatis添加学生:"+name);
        return 0;
    }

    @Override
    public void update(String name) {
        System.out.println("Mybatis更新学生:"+name);
    }
}

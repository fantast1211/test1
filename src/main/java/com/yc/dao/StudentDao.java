package com.yc.dao;

import org.springframework.stereotype.Repository;

@Repository  //加了这个注解 ，这个类可以被Spring容器托管
public interface StudentDao {
    public int add(String name);
    public void update(String name);
}

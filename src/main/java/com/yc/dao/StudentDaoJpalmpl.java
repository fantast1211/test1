package com.yc.dao;

import org.springframework.stereotype.Repository;

@Repository
public class StudentDaoJpalmpl implements StudentDao {
    @Override
    public int add(String name) {
        System.out.println("Jpal添加学生:"+name);
        return 0;
    }

    @Override
    public void update(String name) {
        System.out.println("Jpal更新学生:"+name);
    }
}

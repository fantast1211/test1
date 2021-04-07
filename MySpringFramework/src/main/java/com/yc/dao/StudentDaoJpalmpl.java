package com.yc.dao;

import com.yc.springframework.steretype.MyRepository;
import org.springframework.stereotype.Repository;

@MyRepository
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

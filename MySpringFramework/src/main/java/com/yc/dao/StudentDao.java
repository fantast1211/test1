package com.yc.dao;

import com.yc.springframework.steretype.MyRepository;
import org.springframework.stereotype.Repository;


public interface StudentDao {
    public int add(String name);
    public void update(String name);
}

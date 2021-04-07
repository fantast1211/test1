package com.yc.biz;

import com.yc.AppConfig;
import com.yc.dao.StudentDao;
import com.yc.dao.StudentDaoMybatis;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StudentBizTestTest extends TestCase {
    private ApplicationContext ac;
    private StudentDao studentDao;
    private StudentBiz studentBiz;

    @Before
    public void setUp() throws Exception {
        ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //1 能否自动完成  实例化对象 ->  IOC 控制反转  ——》  容器实例化对象，由容器完成
      //  studentDao = new StudentDaoMybatis();

        //studentBiz = new StudentBiz
       // studentBiz= new StudentBiz(studentDao);
        studentDao=(StudentDaoMybatis)ac.getBean("studentDaoMybatis");
        studentBiz=(StudentBiz)ac.getBean("studentBiz");
        studentBiz.setStudentDao(studentDao);


    }


    public void testAdd(){
        studentDao.add("张三");
    }

    public void testUpdate(){
        studentDao.update("张三");
    }

    public void testBizAdd(){
        studentBiz.add("张三");
    }



}
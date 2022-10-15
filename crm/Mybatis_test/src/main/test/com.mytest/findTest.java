package com.mytest;

import com.crmstudy.domain.User;
import com.crmstudy.mapper.settings.qx.user.UserMapper;
import com.crmstudy.service.settings.qx.user.impl.UserServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;


public class findTest {

    @Test
    public void TestFind1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-Mapper.xml");
        SqlSessionFactory factory = context.getBean("sqlSessionFactory",SqlSessionFactory.class);
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.SelectAllUser();
        for (User user : users) {
            System.out.println(user);
        }
    }


    @Test
    public void TestFind2() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-Service.xml");
        UserServiceImpl userServiceImpl = (UserServiceImpl)context.getBean("userServiceImpl");
        List<User> users = userServiceImpl.SelectAllUser();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void TestFind3() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-Mapper.xml");
        UserMapper userMapper = (UserMapper)context.getBean("userMapper");
        List<User> users = userMapper.SelectAllUser();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void TestFind4() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-Mapper.xml");
        UserMapper userMapper = (UserMapper)context.getBean("userMapper");
        HashMap map = new HashMap();
        map.put("loginAct","ls");
        map.put("loginPwd","yf123");
        User user = userMapper.SelectUserByActNameAndActPwd(map);
        System.out.println(user);
    }
}

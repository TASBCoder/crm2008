package com.crmstudy.service.settings.qx.user.impl;

import com.crmstudy.domain.User;
import com.crmstudy.mapper.settings.qx.user.UserMapper;
import com.crmstudy.service.settings.qx.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    public User SelectUserByActNameAndActPwd(Map<String,Object> map) {
        User user = mapper.SelectUserByActNameAndActPwd(map);
        return user;
    }


    public List<User> SelectAllUser() {
        return mapper.SelectAllUser();
    }
}

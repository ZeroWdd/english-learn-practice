package com.learn.web.service;

import com.learn.web.mapper.UserMapper;
import com.learn.web.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2020/1/13 12:40
 * @Description:
 */
@Service
public class UserService{

    @Autowired
    private UserMapper userMapper;

    public int insertUser(User user) {
        return userMapper.insert(user);
    }
}

package com.learn.web.service;

import com.learn.web.mapper.UserMapper;
import com.learn.web.pojo.User;
import com.learn.web.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Auther: wdd
 * @Date: 2020/1/13 12:40
 * @Description:
 */
@Service
public class UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HttpSession session;

    public int register(User user) {
        return userMapper.insert(user);
    }

    public boolean hasUsername(User user) {
        User one = userMapper.selectOne(user);
        return one != null;
    }

    public int login(User user) {
        User one = userMapper.selectOne(user);
        if(one != null){
            session.setAttribute(Const.USER,one);
        }
        return one == null ? 0 : 1;
    }
}

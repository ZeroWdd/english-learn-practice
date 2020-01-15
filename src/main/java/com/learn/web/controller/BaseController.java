package com.learn.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: wdd
 * @Date: 2020/1/12 15:53
 * @Description:
 */
@Controller
public class BaseController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/user/login")
    public String login(){
        return "user/login";
    }

    @RequestMapping("/user/reg")
    public String reg(){
        return "user/reg";
    }

    @RequestMapping("/learn")
    public String learn(){
        return "learn/index";
    }
}

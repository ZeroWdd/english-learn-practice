package com.learn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: wdd
 * @Date: 2020/1/12 15:09
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.learn.web.mapper")
public class LearnWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearnWebApplication.class);
    }
}

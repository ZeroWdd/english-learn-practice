package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:05
 * @Description: 题目
 */
@Data
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title; // 题目
    private Integer type; // 单选1 多选 2
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;
    private String score;
}

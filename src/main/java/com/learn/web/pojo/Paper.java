package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2020/2/26 11:10
 * @Description:
 */
@Data
@Table(name = "paper")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String score;
}

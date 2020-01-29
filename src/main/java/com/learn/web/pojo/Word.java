package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2020/1/23 17:15
 * @Description:
 */
@Data
@Table(name = "word")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String english;
    private String chinese;
    private String cetFour = "0";
    private String cetSix = "0";
    private String netem = "0";

}

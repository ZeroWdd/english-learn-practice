package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wdd
 * @Date: 2020/2/2 10:58
 * @Description:
 */
@Data
@Table(name = "`read`")
public class Read {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String english;
    private String chinese;
    private String title;
    private String titleChinese;


}

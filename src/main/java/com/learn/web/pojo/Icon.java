package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Classname Icon
 * @Description None
 * @Date 2019/7/22 11:16
 * @Created by WDD
 */
@Data
@Table(name = "icon")
public class Icon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;

}

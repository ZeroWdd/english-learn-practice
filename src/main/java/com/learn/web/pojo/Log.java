package com.learn.web.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Classname Log
 * @Description None
 * @Date 2019/7/24 9:51
 * @Created by WDD
 */
@Data
@Component
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String logTime;
    private String username;
    private String uri;

}

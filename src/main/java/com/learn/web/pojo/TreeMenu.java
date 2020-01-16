package com.learn.web.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname TreeMenu
 * @Description 菜单树
 * @Date 2019/7/16 23:09
 * @Created by WDD
 */
@Data
@Table(name = "treemenu")
public class TreeMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer pid;

    private String name;

    private String icon;

    private String url;

    //添加元素
    @Transient
    private Boolean checked = false;
    private List<TreeMenu> children = new ArrayList<>();

}

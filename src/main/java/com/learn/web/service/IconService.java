package com.learn.web.service;

import com.learn.web.mapper.IconMapper;
import com.learn.web.pojo.Icon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:28
 * @Description:
 */
@Service
public class IconService {
    @Autowired
    private IconMapper iconMapper;

    public List<Icon> selectAll() {
        return iconMapper.selectAll();
    }
}

package com.learn.web.service;

import com.learn.web.mapper.ReadMapper;
import com.learn.web.pojo.Read;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020/2/2 11:01
 * @Description:
 */
@Service
public class ReadService {
    @Autowired
    private ReadMapper readMapper;

    public List<Read> selectListByNum(Integer num) {
        List<Read> reads = readMapper.selectAll();
        List<Read> list = reads.stream().limit(num).collect(Collectors.toList());
        return list;
    }

    public Read selectOne(String id) {
        return readMapper.selectByPrimaryKey(id);
    }
}

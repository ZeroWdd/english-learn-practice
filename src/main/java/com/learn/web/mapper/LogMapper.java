package com.learn.web.mapper;

import com.learn.web.pojo.Log;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:26
 * @Description:
 */
public interface LogMapper extends Mapper<Log> {
    List<Log> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);
}

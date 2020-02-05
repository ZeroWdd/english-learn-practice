package com.learn.web.mapper;

import com.learn.web.pojo.Read;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/2 11:01
 * @Description:
 */
public interface ReadMapper extends Mapper<Read>, IdsMapper<Read> {
    List<Read> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);
}

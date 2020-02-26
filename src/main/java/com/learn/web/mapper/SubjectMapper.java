package com.learn.web.mapper;

import com.learn.web.pojo.Subject;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:08
 * @Description:
 */
public interface SubjectMapper extends Mapper<Subject>, IdsMapper<Subject>, MySqlMapper<Subject> {
    List<Subject> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);
}

package com.learn.web.mapper;

import com.learn.web.pojo.Subject;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:08
 * @Description:
 */
public interface SubjectMapper extends Mapper<Subject>, IdsMapper<Subject> {
}

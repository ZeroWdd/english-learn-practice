package com.learn.web.mapper;

import com.learn.web.pojo.Word;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/23 17:15
 * @Description:
 */
public interface WordMapper extends Mapper<Word>, IdsMapper<Word>, MySqlMapper<Word> {
    List<Word> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);
}

package com.learn.web.mapper;

import com.learn.web.pojo.Paper;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/26 11:17
 * @Description:
 */
public interface PaperMapper extends Mapper<Paper>, IdsMapper<Paper>, MySqlMapper<Paper> {
    List<Paper> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    @Insert("insert into paper_subject(paper_id,subject_id) values(#{paperId},#{subjectId}) ")
    void insertPaperSubject(Integer paperId, Integer subjectId);
}

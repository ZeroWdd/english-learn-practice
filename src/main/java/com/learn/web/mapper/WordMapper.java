package com.learn.web.mapper;

import com.learn.web.pojo.Word;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    @Delete("delete from word")
    void deleteAll();

    @Select("select word.* from word " +
            "where word.cet_four = #{cetFour} " +
            "and word.cet_six = #{cetSix}" +
            "and word.netem = #{netem}")
    List<Word> selectSomeByType(@Param("cetFour")String cetFour, @Param("cetSix")String cetSix, @Param("netem")String netem);

    @Select("select word.* from word,user_word where user_word.user_id = #{id} and user_word.word_id = word.id")
    List<Word> selectByUserId(Integer id);
}

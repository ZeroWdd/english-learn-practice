package com.learn.web.service;

import com.learn.web.mapper.WordMapper;
import com.learn.web.pojo.Log;
import com.learn.web.pojo.Word;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/23 17:14
 * @Description:
 */
@Service
public class WordService {
    @Autowired
    private WordMapper wordMapper;

    public PageBean<Word> queryPage(Map<String, Object> paramMap) {
        PageBean<Word> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Word> datas = wordMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = wordMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public Word selectByEnglish(String english) {
        Word word = new Word();
        word.setEnglish(english);
        return wordMapper.selectOne(word);
    }

    public int editByWord(Word word) {
        return wordMapper.updateByPrimaryKey(word);
    }

    public int insertWord(Word word) {
        return wordMapper.insertSelective(word);
    }

    public Word selectById(Integer id) {
        return wordMapper.selectByPrimaryKey(id);
    }

    public int delByWordIds(List<Integer> idList) {
        String ids = StringUtils.join(idList, ",");
        return wordMapper.deleteByIds(ids);
    }

    public int insertList(List<Word> words) {
        return wordMapper.insertList(words);
    }
}

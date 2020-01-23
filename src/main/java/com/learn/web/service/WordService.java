package com.learn.web.service;

import com.learn.web.mapper.WordMapper;
import com.learn.web.pojo.Log;
import com.learn.web.pojo.Word;
import com.learn.web.util.PageBean;
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
}

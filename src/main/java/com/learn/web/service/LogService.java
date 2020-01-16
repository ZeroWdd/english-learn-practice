package com.learn.web.service;

import com.learn.web.mapper.LogMapper;
import com.learn.web.pojo.Log;
import com.learn.web.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:29
 * @Description:
 */
@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;

    public PageBean<Log> queryPage(Map<String, Object> paramMap) {
        PageBean<Log> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Log> datas = logMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = logMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public int delByLogIds(List<Integer> ids) {
        int count = 0;
        for(Integer id : ids){
            logMapper.deleteByPrimaryKey(id);
            count++;
        }
        return count;
    }
}

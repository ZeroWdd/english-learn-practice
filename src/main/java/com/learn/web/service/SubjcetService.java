package com.learn.web.service;

import com.learn.web.mapper.SubjectMapper;
import com.learn.web.pojo.Read;
import com.learn.web.pojo.Subject;
import com.learn.web.util.DateUtil;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:09
 * @Description:
 */
@Service
public class SubjcetService {
    @Autowired
    private SubjectMapper subjectMapper;

    public PageBean<Subject> queryPage(Map<String, Object> paramMap) {
        PageBean<Subject> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Subject> datas = subjectMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = subjectMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public int delBySubjectIds(List<Integer> idList) {
        String ids = StringUtils.join(idList, ",");
        return subjectMapper.deleteByIds(ids);
    }

    public Subject selectById(Integer id) {
        return subjectMapper.selectByPrimaryKey(id);
    }

    public int editBySubject(Subject subject) {
        if(subject.getAnswer().length() != 1) {
            subject.setType(2);
        }else {
            subject.setType(1);
        }
        subject.setAnswer(subject.getAnswer().toUpperCase());
        return subjectMapper.updateByPrimaryKey(subject);
    }

    public int insertSubject(Subject subject) {
        if(subject.getAnswer().length() != 1) {
            subject.setType(2);
        }else {
            subject.setType(1);
        }
        subject.setAnswer(subject.getAnswer().toUpperCase());
        return subjectMapper.insert(subject);
    }

    public void insertList(List<Subject> subjects) {
        subjectMapper.insertList(subjects);
    }
}

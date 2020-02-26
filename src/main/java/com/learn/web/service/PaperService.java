package com.learn.web.service;

import com.learn.web.mapper.PaperMapper;
import com.learn.web.mapper.SubjectMapper;
import com.learn.web.pojo.Paper;
import com.learn.web.pojo.Subject;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020/2/26 11:18
 * @Description:
 */
@Service
public class PaperService {
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private SubjectMapper subjectMapper;

    private static Random random = new Random();
    static int num = 1;

    public PageBean<Paper> queryPage(Map<String, Object> paramMap) {
        PageBean<Paper> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Paper> datas = paperMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = paperMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public int delByPaperIds(List<Integer> idList) {
        String ids = StringUtils.join(idList, ",");
        return paperMapper.deleteByIds(ids);
    }

    @Transactional
    public boolean addByRandom() {
        try {
            List<Subject> subjects = subjectMapper.selectAll();
            List<Subject> list = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                list.add(subjects.get(random.nextInt(subjects.size())));
            }
            Paper paper = new Paper();
            Optional<Integer> score = list.stream().map(subject -> Integer.valueOf(subject.getScore())).reduce(Integer::sum);
            paper.setScore(score.get().toString());
            paperMapper.insert(paper);
            paper.setTitle("单元测试" + paper.getId());
            paperMapper.updateByPrimaryKey(paper);
            list.stream().map(Subject::getId).forEach(id -> paperMapper.insertPaperSubject(paper.getId(),id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

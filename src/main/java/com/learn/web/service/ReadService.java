package com.learn.web.service;

import com.learn.web.mapper.ReadMapper;
import com.learn.web.pojo.Log;
import com.learn.web.pojo.Read;
import com.learn.web.util.Data;
import com.learn.web.util.DateUtil;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020/2/2 11:01
 * @Description:
 */
@Service
public class ReadService {
    @Autowired
    private ReadMapper readMapper;

    public List<Read> selectListByNum(Integer num) {
        List<Read> reads = readMapper.selectAll();
        List<Read> list = reads.stream().limit(num).collect(Collectors.toList());
        return list;
    }

    public Read selectOne(String id) {
        return readMapper.selectByPrimaryKey(id);
    }

    public PageBean<Read> queryPage(Map<String, Object> paramMap) {
        PageBean<Read> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Read> datas = readMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = readMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public Read selectById(Integer id) {
        return readMapper.selectByPrimaryKey(id);
    }

    public int editByRead(Read read) {
        String stringDate = DateUtil.getStringDate("yyyy-MM-dd HH:mm:ss");
        read.setReadTime(stringDate);
        return readMapper.updateByPrimaryKey(read);
    }

    public int insertRead(Read read) {
        String stringDate = DateUtil.getStringDate("yyyy-MM-dd HH:mm:ss");
        read.setReadTime(stringDate);
        return readMapper.insert(read);
    }

    public int delByReadIds(List<Integer> idList) {
        String ids = StringUtils.join(idList, ",");
        return readMapper.deleteByIds(ids);
    }

    public String nextRead(String readId) {
        Read read = readMapper.selectByPrimaryKey(readId);
        List<Read> reads = readMapper.selectAll();
        Optional<Read> any = reads.stream().filter(read1 -> read1.getId() > read.getId()).findAny();
        if (!any.isPresent()) return reads.get(0).getId().toString();
        return any.get().getId().toString();
    }
}

package com.learn.web.service;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.learn.web.mapper.WordMapper;

import com.learn.web.pojo.Word;
import com.learn.web.util.Const;
import com.learn.web.util.PageBean;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Auther: wdd
 * @Date: 2020/1/23 17:14
 * @Description:
 */
@Service
public class WordService {
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private Gson gson;

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

    // 对redis进行操作，并不对mysql进行操作
    public synchronized void insertRedisWordByList(List<Word> words){
        // 判断redis缓存中是否存在
        Map map = gson.fromJson(redisTemplate.opsForValue().get(Const.WORD_MAP), HashMap.class);
        if(map == null){
            // 将list转为map
            List<Word> list = wordMapper.selectAll();
            if(ArrayUtils.isNotEmpty(list.toArray())){
                map = list.stream().collect(Collectors.toMap(Word::getEnglish, word -> word, (oldValue, newValue) -> newValue));
                redisTemplate.opsForValue().set(Const.WORD_MAP, JSON.toJSONString(map));
            }
        }
        // 逐条添加
        Map<String, Word> wordMap = words.stream().collect(Collectors.toMap(Word::getEnglish, word -> word, (oldValue, newValue) -> newValue));
        if(map != null){
            wordMap.putAll(map);
        }
        redisTemplate.opsForValue().set(Const.WORD_MAP,JSON.toJSONString(wordMap));
    }

    public void insertListByRedis() {
        // 使用stream将map转为list
        Map map = gson.fromJson(redisTemplate.opsForValue().get(Const.WORD_MAP), HashMap.class);
        List<Word> words = new ArrayList<>();
        for(Object o : map.values()){
            Word word = gson.fromJson(gson.toJson(o), Word.class);
            words.add(word);
        }
        // 删除数据库中数据，重新添加
        wordMapper.deleteAll();
        wordMapper.insertList(words);
        // 添加成功后将redis中数据删除
        redisTemplate.delete(Const.WORD_MAP);
    }
}

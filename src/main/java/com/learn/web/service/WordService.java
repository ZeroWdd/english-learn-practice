package com.learn.web.service;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.web.mapper.WordMapper;

import com.learn.web.pojo.User;
import com.learn.web.pojo.Word;
import com.learn.web.util.Const;
import com.learn.web.util.PageBean;

import io.swagger.models.auth.In;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;


import javax.servlet.http.HttpSession;
import java.util.*;
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
    @Autowired
    private HttpSession session;

    private static Random random = new Random();


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
        // 将map转为list
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


    public int selectNumByCetFour() {
        Example example = new Example(Word.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cetFour","1");
        return wordMapper.selectCountByExample(example);
    }

    public int selectNumByCetSix() {
        Example example = new Example(Word.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cetSix","1");
        return wordMapper.selectCountByExample(example);
    }

    public int selectNumByNetem() {
        Example example = new Example(Word.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("netem","1");
        return wordMapper.selectCountByExample(example);
    }

    /**
     * 随机抽取20个单词
     * @param type
     * @return
     */
    public List<Word> selectSome(String type) {
        try {
            // 首先看看redis缓存是否有数据
            User user = (User) session.getAttribute(Const.USER);
            String json = redisTemplate.opsForValue().get(Const.WORD_LIST_BY_USER_ID_AND_TYPE + user.getId() + "_" + type);
            List<Word> fromJson = gson.fromJson(json, new TypeToken<List<Word>>(){}.getType());
            if(StringUtils.isEmpty(json) || fromJson.size() == 0){
                List<Word> words = new ArrayList();
                Example example = new Example(Word.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo(type,"1");
                words = wordMapper.selectByExample(example);
                // 调处用户学习了的
                List<Word> wordsByUserId = wordMapper.selectByUserId(user.getId());
                //删选
                if(ArrayUtils.isNotEmpty(wordsByUserId.toArray())){
                    for(Word word : words){
                        for(Word w : wordsByUserId){
                            if(word.getId().equals(w.getId())){
                                words.remove(word);
                            }
                        }
                    }
                }

                // 筛选完后,随机抽取20个单词,存入redis
                List<Word> list = new ArrayList<>();
                if(words.size() <= 20){
                    list.addAll(words);
                }else{
                    for(int i = 0; i < 20; i++){
                        list.add(words.get(random.nextInt(words.size())));
                    }
                }
                redisTemplate.opsForValue().set(Const.WORD_LIST_BY_USER_ID_AND_TYPE + user.getId() + "_" + type,gson.toJson(list));
                return list;
            }
            return gson.fromJson(json, new TypeToken<List<Word>>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 学习了一个单词
     * @param type
     * @param word
     * @return
     */
    public boolean studyOne(String type,String word) {
        // 从redis中删除
        User user = (User) session.getAttribute(Const.USER);
        String json = redisTemplate.opsForValue().get(Const.WORD_LIST_BY_USER_ID_AND_TYPE + user.getId() + "_" + type);
        List<Word> words = gson.fromJson(json, new TypeToken<List<Word>>(){}.getType());
        boolean flag = false;
        Integer wordId = -1;
        for(Word w : words){
            if(w.getEnglish().equals(word)){
                wordId = w.getId();
                words.remove(w);
                flag = true;
                break;
            }
        }
        if(flag){
            redisTemplate.opsForValue().set(Const.WORD_LIST_BY_USER_ID_AND_TYPE + user.getId() + "_" + type,gson.toJson(words));
            // 保存到mysql
            wordMapper.insertUserWord(user.getId(), wordId);
        }
        return flag;
    }

    /**
     * 换一组单词
     * @param type
     * @return
     */
    public boolean changeGroup(String type) {
        // 先删除redis中数据
        User user = (User) session.getAttribute(Const.USER);
        redisTemplate.delete(Const.WORD_LIST_BY_USER_ID_AND_TYPE + user.getId() + "_" + type);
        // 直接返回true 刷新页面即可
        return true;
    }
}

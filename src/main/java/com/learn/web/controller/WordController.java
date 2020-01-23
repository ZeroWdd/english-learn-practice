package com.learn.web.controller;

import com.learn.web.pojo.Admin;
import com.learn.web.pojo.Word;
import com.learn.web.service.WordService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/23 17:13
 * @Description:
 */
@RestController
public class WordController {

    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private WordService wordService;

    @RequestMapping("/manager/wordList")
    public Object wordList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String english,String chinese,String cetFour,String cetSix,String netem){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(english)) paramMap.put("english",english);
        if(!StringUtils.isEmpty(chinese)) paramMap.put("chinese",chinese);
        if(!StringUtils.isEmpty(cetFour)) paramMap.put("cetFour",cetFour);
        if(!StringUtils.isEmpty(cetSix)) paramMap.put("cetSix",cetSix);
        if(!StringUtils.isEmpty(netem)) paramMap.put("netem",netem);


        PageBean<Word> pageBean = wordService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }
}

package com.learn.web.controller;

import com.learn.web.pojo.Log;
import com.learn.web.pojo.Read;
import com.learn.web.service.ReadService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/2 11:02
 * @Description:
 */
@RestController
public class ReadController {
    @Autowired
    private ReadService readService;

    @Autowired
    private AjaxResult ajaxResult;

    @GetMapping("learn/read/{num}")
    public AjaxResult read(@PathVariable Integer num){
        List<Read> reads = readService.selectListByNum(num);
        if(ArrayUtils.isNotEmpty(reads.toArray())){
            ajaxResult.setData(reads);
            return ajaxResult.ajaxSuccess();
        }
        return ajaxResult.ajaxFalse();
    }

    @RequestMapping("/manager/readList")
    public Object adminList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String readTime){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(readTime)){
            String[] split = readTime.split(" - ");
            paramMap.put("stime",split[0]);
            paramMap.put("ftime",split[1]);
        }

        PageBean<Read> pageBean = readService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }
}

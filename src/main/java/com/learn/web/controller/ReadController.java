package com.learn.web.controller;

import com.learn.web.pojo.Read;
import com.learn.web.service.ReadService;
import com.learn.web.util.AjaxResult;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

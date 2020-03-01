package com.learn.web.controller;

import com.learn.web.pojo.Paper;
import com.learn.web.service.PaperService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Data;
import com.learn.web.util.PageBean;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/26 11:18
 * @Description:
 */
@RestController
public class PaperController {

    @Autowired
    private PaperService paperService;
    @Autowired
    private AjaxResult ajaxResult;

    @RequestMapping("/manager/paperList")
    public Object paperList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                              @RequestParam(value = "limit", defaultValue = "5") Integer pagesize){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        PageBean<Paper> pageBean = paperService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }

    @PostMapping("/manager/delPaper")
    public AjaxResult delPaper(Data data){
        int count = paperService.delByPaperIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

    @PostMapping("/manager/addPaper")
    public AjaxResult addPaper(){
        boolean flag = paperService.addByRandom();
        if (!flag) {
            ajaxResult.ajaxFalse("试卷生成失败");
        } else {
            ajaxResult.ajaxSuccess("试卷生成成功");
        }
        return ajaxResult;
    }

    @GetMapping("exam/paper/{num}")
    public AjaxResult paper(@PathVariable Integer num){
        List<Paper> papers = paperService.selectListByNum(num);
        if(ArrayUtils.isNotEmpty(papers.toArray())){
            ajaxResult.setData(papers);
            return ajaxResult.ajaxSuccess();
        }
        return ajaxResult.ajaxFalse();
    }
}

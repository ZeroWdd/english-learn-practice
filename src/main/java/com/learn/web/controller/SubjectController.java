package com.learn.web.controller;

import com.learn.web.pojo.Read;
import com.learn.web.pojo.Subject;
import com.learn.web.pojo.Word;
import com.learn.web.service.SubjcetService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Data;
import com.learn.web.util.ListSplitUtil;
import com.learn.web.util.PageBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:05
 * @Description:
 */
@RestController
public class SubjectController {
    @Autowired
    private SubjcetService subjcetService;
    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private ListSplitUtil splitUtil;

    @RequestMapping("/manager/subjectList")
    public Object subjectList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            Integer type){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(type)){
            paramMap.put("type",type);
        }


        PageBean<Subject> pageBean = subjcetService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }

    @PostMapping("/manager/delSubject")
    public AjaxResult delSubject(Data data){
        int count = subjcetService.delBySubjectIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

    @PostMapping("/manager/addSubject")
    public AjaxResult submitAddSubject(Subject subject){
        if(subject.getId() !=null){
            //修改角色
            int count = subjcetService.editBySubject(subject);
            if(count > 0){
                ajaxResult.ajaxSuccess("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加角色
            int count = subjcetService.insertSubject(subject);
            if(count > 0){
                ajaxResult.ajaxSuccess("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }

    @RequestMapping("/manager/subject/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file){
        try {
            Workbook workbook;
            String name = org.apache.commons.lang3.StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            if("xls".equals(name)){
                workbook = new HSSFWorkbook(new BufferedInputStream(file.getInputStream()));
            }else if("xlsx".equals(name)) {
                workbook = new XSSFWorkbook(new BufferedInputStream(file.getInputStream()));
            }else{
                ajaxResult.ajaxFalse("格式不正确");
                return ajaxResult;
            }
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0); // 获取第0列数据
            int title = -1; // 题目索引列
            int a = -1; //
            int b = -1; //
            int c = -1; //
            int d = -1; //
            int answer = -1; //
            int score = -1; //
            for(int i = 0; i < row.getLastCellNum(); i++){
                Cell cell = row.getCell(i);
                if(cell != null && cell.getStringCellValue().equals("title")){
                    title = i; //
                }
                if(cell != null && cell.getStringCellValue().equalsIgnoreCase("a")){
                    a = i; //
                }
                if(cell != null && cell.getStringCellValue().equalsIgnoreCase("b")){
                    b = i; //
                }
                if(cell != null && cell.getStringCellValue().equalsIgnoreCase("c")){
                    c = i; //
                }
                if(cell != null && cell.getStringCellValue().equalsIgnoreCase("d")){
                    d = i; //
                }
                if(cell != null && cell.getStringCellValue().equals("answer")){
                    answer = i; //
                }
                if(cell != null && cell.getStringCellValue().equals("score")){
                    score = i; //
                }
            }
            if(title == -1 || a == -1 || b == -1 || c == -1 || d == -1 || answer == -1 || score == -1){
                ajaxResult.ajaxFalse("格式不正确");
                return ajaxResult;
            }
            List<Subject> subjects = new ArrayList<>();  // 将文件放入列表
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
                row = sheet.getRow(rowNum);// 获取第rowNum行
                Subject subject = new Subject();
                subject.setTitle(row.getCell(title).getStringCellValue());
                subject.setA(row.getCell(a).getStringCellValue());
                subject.setB(row.getCell(b).getStringCellValue());
                subject.setC(row.getCell(c).getStringCellValue());
                subject.setD(row.getCell(d).getStringCellValue());
                subject.setAnswer(row.getCell(answer).getStringCellValue());
                row.getCell(score).setCellType(Cell.CELL_TYPE_STRING);
                subject.setScore(row.getCell(score).getStringCellValue());
                if(subject.getAnswer().length() == 1) {
                    subject.setType(1);
                }else {
                    subject.setType(2);
                }
                subjects.add(subject);
            }
            subjcetService.insertList(subjects);

        } catch (IOException e) {
            return ajaxResult.ajaxFalse();
        }
        return ajaxResult.ajaxSuccess();
    }
}

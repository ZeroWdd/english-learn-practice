package com.learn.web.controller;

import com.learn.web.pojo.Word;
import com.learn.web.service.WordService;
import com.learn.web.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    private ListSplitUtil splitUtil;
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

    @PostMapping("/manager/addWord")
    public AjaxResult submitAddWord(@RequestBody Word word){
        if(word.getId() !=null){
            int count = wordService.editByWord(word);
            if(count > 0){
                ajaxResult.ajaxSuccess("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            Word w = wordService.selectByEnglish(word.getEnglish());
            if(w != null){
                //与修改用户名一样，但存在数据库中，表示后来改的用户名已存在
                ajaxResult.ajaxFalse("单词已存在");
                return ajaxResult;
            }
            int count = wordService.insertWord(word);
            if(count > 0){
                ajaxResult.ajaxSuccess("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }

    @PostMapping("/manager/delWord")
    public AjaxResult delWord(Data data){
        int count = wordService.delByWordIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

    @RequestMapping("/manager/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file){
        try {
            Workbook workbook;
            String name = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
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
            int englishIndex = -1; // 英译索引列
            int chineseIndex = -1; // 汉译索引列
            int cetFourIndex = -1; // cet-4索引列
            int cetSixIndex = -1; // cet-6索引列
            int netemIndex = -1; // 考研英语索引列
            for(int i = 0; i < row.getLastCellNum(); i++){
                Cell cell = row.getCell(i);
                if(cell != null && cell.getStringCellValue().equals("英译")){
                    englishIndex = i; // 记录英译实在哪一列
                }
                if(cell != null && cell.getStringCellValue().equals("汉译")){
                    chineseIndex = i; // 记录汉译实在哪一列
                }
            }
            if(englishIndex == -1 || chineseIndex == -1){
                ajaxResult.ajaxFalse("格式不正确");
                return ajaxResult;
            }
            List<Word> words = new ArrayList<>();  // 将文件放入列表
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
                row = sheet.getRow(rowNum);// 获取第rowNum行
                if(row.getCell(englishIndex) == null || row.getCell(chineseIndex) == null){
                    ajaxResult.ajaxFalse("数据缺失");
                    return ajaxResult;
                }
                Word word = new Word();
                word.setEnglish(row.getCell(englishIndex).getStringCellValue());
                word.setChinese(row.getCell(chineseIndex).getStringCellValue());
                if(cetFourIndex != -1){
                    word.setCetFour(row.getCell(cetFourIndex).getStringCellValue());
                }
                if(cetSixIndex != -1){
                    word.setCetSix(row.getCell(cetSixIndex).getStringCellValue());
                }
                if(netemIndex != -1){
                    word.setNetem(row.getCell(netemIndex).getStringCellValue());
                }
                words.add(word);
            }

            // 创建n个线程
            int n = 10;
            List<Thread> threads = new ArrayList<>();
            List<List<Word>> lists = splitUtil.averageAssign(words, n);
            for(int i = 0; i < n; i++){
                int finalI = i;
                Thread thread = new Thread(() -> {
                    wordService.insertRedisWordByList(lists.get(finalI));
                });
                thread.start();
                threads.add(thread);
            }
            for(Thread thread : threads) thread.join();
            // 最后将redis中的数据存入数据库
            wordService.insertListByRedis();

        } catch (IOException | InterruptedException e) {
            return ajaxResult.ajaxFalse();
        }
        return ajaxResult.ajaxSuccess();
    }



}

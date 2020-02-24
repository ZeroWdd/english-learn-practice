package com.learn.web.service;

import com.learn.web.mapper.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:09
 * @Description:
 */
@Service
public class SubjcetService {
    @Autowired
    private SubjectMapper subjectMapper;
}

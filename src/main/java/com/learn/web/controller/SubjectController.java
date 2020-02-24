package com.learn.web.controller;

import com.learn.web.service.SubjcetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wdd
 * @Date: 2020/2/23 17:05
 * @Description:
 */
@RestController
public class SubjectController {
    @Autowired
    private SubjcetService subjcetService;
}

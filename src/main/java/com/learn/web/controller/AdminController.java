package com.learn.web.controller;

import com.learn.web.pojo.Admin;
import com.learn.web.service.AdminService;
import com.learn.web.service.RoleService;
import com.learn.web.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:12
 * @Description:
 */
@RestController
@RequestMapping("/manager")
public class AdminController {
    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private AdminService adminService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    /**
     * 异步加载管理员列表
     * @param pageno
     * @param pagesize
     * @param username
     * @param phone
     * @param email
     * @return
     */
    @RequestMapping("/adminList")
    public Object adminList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String username,String phone,String email,String rid){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(username)) paramMap.put("username",username);
        if(!StringUtils.isEmpty(phone)) paramMap.put("phone",phone);
        if(!StringUtils.isEmpty(email)) paramMap.put("email",email);
        if(!StringUtils.isEmpty(rid) && !rid.equals("0")) paramMap.put("rid",rid);

        PageBean<Admin> pageBean = adminService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }


    /**
     * 添加管理员  修改管理员
     * @param admin
     * @param status
     * @return
     */
    @PostMapping("/addAdmin")
    public AjaxResult submitAddAdmin(Admin admin,String status){
        Admin byName = adminService.selectByName(admin.getUsername());
        Admin byEmail = adminService.selectByEmail(admin.getEmail());
        if(admin.getRid().equals(0)){
            ajaxResult.ajaxFalse("请选择角色");
            return ajaxResult;
        }
        //on 表示通过 null 表示待审核
        admin.setStatus(status != null?1:0);
        if(admin.getId() !=null){
            //修改管理员
            if(byName != null && !byName.getId().equals(admin.getId())){
                //与修改用户名一样，但存在数据库中，表示后来改的用户名已存在
                ajaxResult.ajaxFalse("用户名已存在");
                return ajaxResult;
            }
            if(byEmail != null && !byEmail.getId().equals(admin.getId())){
                //与修改邮箱一样，但存在数据库中，表示后来改的邮箱已存在
                ajaxResult.ajaxFalse("邮箱已存在");
                return ajaxResult;
            }
            int count = adminService.editByAdmin(admin);
            if(count > 0){
                ajaxResult.ajaxSuccess("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加管理员
            if(byName != null){
                //与原用户名不一样，但存在数据库中，表示后来改的用户名已存在
                ajaxResult.ajaxFalse("用户名已存在");
                return ajaxResult;
            }
            if(byEmail != null){
                //与原邮箱不一样，但存在数据库中，表示后来改的邮箱已存在
                ajaxResult.ajaxFalse("邮箱已存在");
                return ajaxResult;
            }
            String stringDate = DateUtil.getStringDate("yyyy-MM-dd");
            admin.setCreatetime(stringDate);
            admin.setPassword(passwordEncoder.encode("123")); // 设置默认密码
            int count = adminService.insertAdmin(admin);
            if(count > 0){
                ajaxResult.ajaxSuccess("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }

    /**
     * 删除管理员
     * @param data
     * @return
     */
    @PostMapping("/delAdmin")
    public AjaxResult delAdmin(Data data){
        int count = adminService.delByAdminIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

    /**
     * 修改密码
     * @param session
     * @param password
     * @param newpassword
     * @param repassword
     * @return
     */
    @PostMapping("/editPassword")
    public AjaxResult editPassword(HttpSession session, String password, String newpassword, String repassword){
        Admin admin = (Admin) session.getAttribute(Const.ADMIN);
        if(!password.equals(admin.getPassword())){
            ajaxResult.ajaxFalse("原密码错误");
            return ajaxResult;
        }
        if(!newpassword.equals(repassword)){
            ajaxResult.ajaxFalse("密码不一致");
            return ajaxResult;
        }
        admin.setPassword(newpassword);
        int count = adminService.editByAdmin(admin);
        if(count >= 1){
            ajaxResult.ajaxSuccess("修改密码成功");
        }else{
            ajaxResult.ajaxFalse("系统错误");
        }
        return ajaxResult;
    }

    @PostMapping("/editInfo")
    public AjaxResult editInfo(Admin admin,HttpSession session){
        Admin ad = (Admin) session.getAttribute(Const.ADMIN);
        ad.setUsername(admin.getUsername());
        ad.setEmail(admin.getEmail());
        ad.setPhone(admin.getPhone());
        int count = adminService.editByAdmin(ad);
        if(count >= 1){
            ajaxResult.ajaxSuccess("修改成功");
        }else{
            ajaxResult.ajaxFalse("修改失败");
        }
        return ajaxResult;
    }

}

package com.learn.web.controller;

import com.learn.web.pojo.Role;
import com.learn.web.service.RoleService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Const;
import com.learn.web.util.Data;
import com.learn.web.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Classname RoleController
 * @Description None
 * @Date 2019/7/19 16:19
 * @Created by WDD
 */
@RestController
@RequestMapping("/manager")
public class RoleController {

    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private RoleService roleService;


    /**
     * 异步加载角色列表
     * @param pageno
     * @param pagesize
     * @param rid
     * @return
     */
    @RequestMapping("/roleList")
    public Object roleList(@RequestParam(value = "page", defaultValue = "1") Integer pageno,
                            @RequestParam(value = "limit", defaultValue = "5") Integer pagesize,
                            String rid){
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno",pageno);
        paramMap.put("pagesize",pagesize);

        //判断是否为空
        if(!StringUtils.isEmpty(rid) && !rid.equals("0")) paramMap.put("rid",rid);

        PageBean<Role> pageBean = roleService.queryPage(paramMap);

        Map<String,Object> rest = new HashMap();
        rest.put("code", 0);
        rest.put("msg", "");
        rest.put("count",pageBean.getTotalsize());
        rest.put("data", pageBean.getDatas());
        return rest;
    }


    /**
     * 删除角色
     * @param data
     * @return
     */
    @PostMapping("/delRole")
    @ResponseBody
    public AjaxResult delRole(Data data){
        int count = roleService.delByRoleIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("存在管理员拥有此角色,无法删除");
        }
        return ajaxResult;
    }


    /**
     * 添加角色
     * @param role
     * @return
     */
    @PostMapping("/addRole")
    public AjaxResult submitAddRole(Role role){
        Role byName = roleService.selectByName(role.getName());
        if(role.getId() !=null){
            //修改角色
            if(byName != null && !byName.getId().equals(role.getId())){
                //与修改角色一样，但存在数据库中，表示后来改的角色已存在
                ajaxResult.ajaxFalse("角色已存在");
                return ajaxResult;
            }
            int count = roleService.editByRole(role);
            if(count > 0){
                ajaxResult.ajaxSuccess("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加角色
            if(byName != null){
                //与原角色不一样，但存在数据库中，表示后来改的角色已存在
                ajaxResult.ajaxFalse("角色已存在");
                return ajaxResult;
            }
            int count = roleService.insertRole(role);
            if(count > 0){
                ajaxResult.ajaxSuccess("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }
}

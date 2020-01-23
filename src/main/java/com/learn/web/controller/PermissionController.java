package com.learn.web.controller;

import com.learn.web.pojo.Admin;
import com.learn.web.pojo.TreeMenu;
import com.learn.web.service.IconService;
import com.learn.web.service.TreeMenuService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Const;
import com.learn.web.util.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @Classname PermissionController
 * @Description None
 * @Date 2019/7/21 9:51
 * @Created by WDD
 */
@RestController
@RequestMapping("/manager")
public class PermissionController {

    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private TreeMenuService treeMenuService;
    @Autowired
    private IconService iconService;

    /**
     * 异步加载权限树
     * @param session
     * @return
     */
    @PostMapping("/treeMenu")
    public Object treeMenu(HttpSession session){
//        if(!StringUtils.isEmpty(session.getAttribute(Const.TREEMENU))){
//            return session.getAttribute(Const.TREEMENU);
//        }
        Admin admin = (Admin) session.getAttribute(Const.ADMIN);
        List<TreeMenu> treeMenuList = treeMenuService.selectByAdminId(admin.getId());
        session.setAttribute(Const.TREEMENU,treeMenuList);
        return treeMenuList;
    }


    /**
     * 异步加载权限树
     * @param session
     * @return
     */
    @GetMapping("/permissionList")
    public Object permissionList(HttpSession session, Integer id){
        List<TreeMenu> treeMenuList = treeMenuService.selectAll();
        if(id != null){
            List<TreeMenu> atList = treeMenuService.selectByRoleId(id);
            for(TreeMenu info : treeMenuList){
                for(TreeMenu at : atList){
                    if(info.getId().equals(at.getId())){
                        info.setChecked(true);
                    }
                }
            }
        }
        HashMap<String, Object> rest = new HashMap<>();
        rest.put("code",0);
        rest.put("msg","ok");
        rest.put("data",treeMenuList);

        return rest;
    }

    /**
     * 添加修改权限
     * @param treeMenu
     * @return
     */
    @PostMapping("/addPermission")
    public AjaxResult submitAddPermission(TreeMenu treeMenu){
        TreeMenu byName = treeMenuService.selectByName(treeMenu.getName());
//        TreeMenu byUrl = treeMenuService.selectByUrl(treeMenu.getUrl());

        if(treeMenu.getId() !=null){
            //修改
            if(byName != null && !byName.getId().equals(treeMenu.getId())){
                ajaxResult.ajaxFalse("权限名已存在");
                return ajaxResult;
            }
            int count = treeMenuService.editByPermission(treeMenu);
            if(count > 0){
                ajaxResult.ajaxSuccess("修改成功");
            }else{
                ajaxResult.ajaxFalse("修改失败");
            }
        }else{
            //添加
            if(byName != null){
                ajaxResult.ajaxFalse("权限名已存在");
                return ajaxResult;
            }
            //若是添加父节点
            if(treeMenu.getPid().equals(-1)){
                treeMenu.setUrl("-1");
            }
            int count = treeMenuService.insertPermission(treeMenu);
            if(count > 0){
                ajaxResult.ajaxSuccess("添加成功");
            }else{
                ajaxResult.ajaxFalse("添加失败");
            }
        }
        return ajaxResult;
    }

    /**
     * 删除权限
     * @param data
     * @return
     */
    @PostMapping("/delPermission")
    public AjaxResult delPermission(Data data){
        List<TreeMenu> treeMenuList = treeMenuService.selectByPid(data.getIds().get(0));
        if(treeMenuList.size() !=0 ){
            ajaxResult.ajaxFalse("请先删除子节点");
            return ajaxResult;
        }
        int count = treeMenuService.delByPermissionIds(data.getIds());
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("删除成功");
        }else{
            ajaxResult.ajaxFalse("删除失败");
        }
        return ajaxResult;
    }

    /**
     * 给角色分配权限
     * @param data
     * @param id
     * @return
     */
    @PostMapping("/allotPer")
    public AjaxResult allotPre(Data data,Integer id){
        int count = treeMenuService.updateRolePermission(data.getIds(),id);
        if(count >= data.getIds().size()){
            ajaxResult.ajaxSuccess("分配成功");
        }
        return ajaxResult;
    }

}

package com.learn.web.service;

import com.learn.web.mapper.TreeMenuMapper;
import com.learn.web.pojo.TreeMenu;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:30
 * @Description:
 */
@Service
public class TreeMenuService {
    @Autowired
    private TreeMenuMapper treeMenuMapper;

    public TreeMenu selectById(Integer id) {
        return treeMenuMapper.selectByPrimaryKey(id);
    }

    public List<TreeMenu> selectByAdminId(Integer id) {
        //获取用户所有的全部权限(父,子权限)菜单
        List<TreeMenu> treeMenuList = treeMenuMapper.selectByAdminId(id);
        //保存所有的父(主)菜单
        List<TreeMenu> root = new ArrayList<>();
        //遍历所有菜单集合,如果是主菜单的话直接放入root集合
        for(TreeMenu treeMenu : treeMenuList){
            //pid为0,则为父(主)菜单
            if(treeMenu.getPid() == -1){
                root.add(treeMenu);
            }
        }
        //这个是遍历所有主菜单,分别获取所有主菜单的所有子菜单
        for(TreeMenu treeMenu : root){
            //获取所有子菜单 递归
            List<TreeMenu> childrenList = getchildrenMeun(treeMenu.getId(),treeMenuList);
            //这个是实体类中的子菜单集合
            treeMenu.setChildren(childrenList);
        }
        return root;
    }

    public List<TreeMenu> selectAll() {
        return treeMenuMapper.selectAll();
    }

    public List<TreeMenu> selectByRoleId(Integer id) {
        return treeMenuMapper.selectByRoleId(id);
    }

    public TreeMenu selectByName(String name) {
        return treeMenuMapper.selectByName(name);
    }

    public TreeMenu selectByUrl(String url) {
        return treeMenuMapper.selectByUrl(url);
    }

    public int editByPermission(TreeMenu treeMenu) {
        return treeMenuMapper.updateByPrimaryKeySelective(treeMenu);
    }

    public int insertPermission(TreeMenu treeMenu) {
        return treeMenuMapper.insert(treeMenu);
    }

    public List<TreeMenu> selectByPid(Integer pid) {
        return treeMenuMapper.selectByPid(pid);
    }

    public int delByPermissionIds(List<Integer> ids) {
        int count = 0;
        for(Integer id : ids){
            treeMenuMapper.deleteByPrimaryKey(id);
            count++;
        }
        return count;
    }

    public int updateRolePermission(List<Integer> ids, Integer id) {
        treeMenuMapper.delRolePermissionByRid(id);
        int count = 0;
        for(Integer tid : ids){
            treeMenuMapper.updateRolePermission(tid,id);
            count++;
        }
        return count;
    }

    //递归获取子菜单
    public List<TreeMenu> getchildrenMeun(int id,List<TreeMenu> allMeun){
        //用于保存子菜单
        List<TreeMenu> childrenList=new ArrayList<>();
        for (TreeMenu info: allMeun){
            //判断当前的菜单标识是否等于主菜单的id
            if(info.getPid()==id){
                //如果是的话 就放入主菜单对象的子菜单集合
                childrenList.add(info);
            }
        }

        //这里就是递归了，遍历所有的子菜单
        for (TreeMenu info:childrenList){
            info.setChildren(getchildrenMeun(info.getId(),allMeun));
        }

        //如果子菜单为空的话，那就说明某菜单下没有子菜单了，直接返回空,子菜单为空的话就不会继续
        //迭代了
        if(childrenList!=null && childrenList.size()==0){
            return null;
        }
        return  childrenList;
    }
}

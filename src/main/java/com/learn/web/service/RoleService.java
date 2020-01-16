package com.learn.web.service;

import com.learn.web.mapper.RoleMapper;
import com.learn.web.pojo.Role;
import com.learn.web.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:29
 * @Description:
 */
@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> selectAll() {
        return roleMapper.selectAll();
    }

    public Role selectById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public PageBean<Role> queryPage(Map<String, Object> paramMap) {
        PageBean<Role> pageBean = new PageBean<>((Integer) paramMap.get("pageno"),(Integer) paramMap.get("pagesize"));

        Integer startIndex = pageBean.getStartIndex();
        paramMap.put("startIndex",startIndex);
        List<Role> datas = roleMapper.queryList(paramMap);
        pageBean.setDatas(datas);

        Integer totalsize = roleMapper.queryCount(paramMap);
        pageBean.setTotalsize(totalsize);
        return pageBean;
    }

    public int delByRoleIds(List<Integer> ids) {
        int count = 0;
        for(Integer id : ids){
            roleMapper.deleteByPrimaryKey(id);
            count++;
        }
        return count;
    }

    public Role selectByName(String name) {
        return roleMapper.selectByName(name);
    }

    public int editByRole(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    public int insertRole(Role role) {
        return roleMapper.insert(role);
    }
}

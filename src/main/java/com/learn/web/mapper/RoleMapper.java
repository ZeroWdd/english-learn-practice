package com.learn.web.mapper;

import com.learn.web.pojo.Role;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:26
 * @Description:
 */
public interface RoleMapper extends Mapper<Role> {
    @Select("select role.id,role.name from role,admin where admin.rid = role.id and admin.id = #{adminId}")
    List<Role> findByAdminId(Integer adminId);

    @Select("select role.id,role.name from role,role_treemenu,treemenu where role.id = role_treemenu.rid " +
            "and role_treemenu.tid = treemenu.id and treemenu.id = #{treeMenuId}")
    List<Role> findByTreeMenuId(Integer treeMenuId);

    List<Role> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    @Select("select * from role where name = #{name}")
    Role selectByName(String name);
}

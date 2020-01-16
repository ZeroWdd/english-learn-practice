package com.learn.web.mapper;

import com.learn.web.pojo.TreeMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:27
 * @Description:
 */
public interface TreeMenuMapper extends Mapper<TreeMenu> {
    @Select("select treemenu.id,treemenu.pid,treemenu.name,treemenu.icon,treemenu.url" +
            "        from admin,role,role_treemenu,treemenu" +
            "        where admin.rid = role.id" +
            "        and role.id = role_treemenu.rid" +
            "        and treemenu.id = role_treemenu.tid" +
            "        and admin.id = #{id}")
    List<TreeMenu> selectByAdminId(Integer id);

    @Select("select treemenu.id,treemenu.pid,treemenu.name,treemenu.icon,treemenu.url" +
            "        from role,role_treemenu,treemenu" +
            "        where role.id = role_treemenu.rid and treemenu.id = role_treemenu.tid and role.id = #{id}")
    List<TreeMenu> selectByRoleId(Integer id);

    @Select("select * from treemenu where name =#{name}")
    TreeMenu selectByName(String name);

    @Select("select * from treemenu where url =#{url}")
    TreeMenu selectByUrl(String url);

    @Select("select * from treemenu where pid = #{pid}")
    List<TreeMenu> selectByPid(Integer pid);

    @Delete("delete from role_treemenu where rid = #{id}")
    void delRolePermissionByRid(Integer id);

    @Update("insert into role_treemenu(rid,tid) values(#{id},#{tid})")
    void updateRolePermission(@Param("tid")Integer tid, @Param("id")Integer id);

    @Select("select treemenu.url from treemenu")
    List<String> selectURLAll();

    @Select("select treemenu.url" +
            "        from admin,role,role_treemenu,treemenu" +
            "        where admin.rid = role.id" +
            "        and role.id = role_treemenu.rid" +
            "        and treemenu.id = role_treemenu.tid" +
            "        and treemenu.pid != -1" +
            "        and admin.username = #{username}")
    List<String> selectByUserName(String username);
}

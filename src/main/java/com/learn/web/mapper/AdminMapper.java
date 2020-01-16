package com.learn.web.mapper;

import com.learn.web.pojo.Admin;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.userdetails.UserDetails;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020/1/16 12:25
 * @Description:
 */
public interface AdminMapper extends Mapper<Admin> {
    @Select("select * from admin where username = #{username}")
    @Results({
            @Result(id = true, property = "id",column = "id"),
            @Result(property = "roles", column = "id", javaType = List.class,
                    many = @Many(select = "com.learn.web.mapper.RoleMapper.findByAdminId"))
    })
    Admin findByName(String username);

    List<Admin> queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    @Select("select * from admin where username = #{username}")
    Admin selectByName(String username);

    @Select("select * from admin where email = #{email}")
    Admin selectByEmail(String email);
}

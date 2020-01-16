package com.learn.web.handler;

import com.alibaba.fastjson.JSON;
import com.learn.web.pojo.Admin;
import com.learn.web.pojo.Role;
import com.learn.web.service.RoleService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2019/12/30 15:04
 * @Description:
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AjaxResult ajaxResult;

    @Autowired
    private RoleService roleService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        Admin ad = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute(Const.ADMIN,ad);
        //获取角色列表，存入session
        if(session.getAttribute(Const.ROLE) == null){
            List<Role> roleList = roleService.selectAll();
            session.setAttribute(Const.ROLE,roleList);
        }

        ajaxResult.ajaxSuccess("登录成功");
        String json = JSON.toJSONString(ajaxResult);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }
}

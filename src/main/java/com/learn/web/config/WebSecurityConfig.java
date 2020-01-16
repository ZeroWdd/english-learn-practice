package com.learn.web.config;

import com.learn.web.handler.LoginFailureHandler;
import com.learn.web.handler.LoginSuccessHandler;
import com.learn.web.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @Auther: wdd
 * @Date: 2019/12/29 14:12
 * @Description:
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AdminService adminService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/manager/login").permitAll()
                .antMatchers("/mystatic/**","/layuiadmin/**","/font-awesome-4.7.0/**","/res/**").permitAll()
                .antMatchers("/manager/**").access("@rbacConfig.hasPermission(request,authentication)")
                .and()
                .formLogin().loginPage("/manager/login").successHandler(loginSuccessHandler).failureHandler(loginFailureHandler)
                .and()
                .headers().frameOptions().disable() // 防止报Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'错误
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminService)
                .passwordEncoder(passwordEncoder());
    }
}

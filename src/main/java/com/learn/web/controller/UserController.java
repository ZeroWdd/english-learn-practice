package com.learn.web.controller;

import com.alibaba.fastjson.JSON;
import com.learn.web.pojo.User;
import com.learn.web.service.UserService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.NumberUtils;
import com.learn.web.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdd
 * @Date: 2020/1/12 17:51
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AjaxResult ajaxResult;
    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    /**
     * 发送验证码给手机
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public AjaxResult code(@RequestParam("phone") String phone){
        String code = NumberUtils.generateCode(4);
        try {
            //发送短信
            HashMap<String, String> msg = new HashMap<>();
            msg.put("code",code);
            smsUtil.sendSms(phone, JSON.toJSONString(msg));
            // 将code存入redis
            redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            ajaxResult.ajaxSuccess(null);
        }catch (Exception e){
            ajaxResult.ajaxFalse(null);
        }
        return ajaxResult;
    }

    /**
     * 校验验证码
     * @param map
     * @return
     */
    @PostMapping("/check/code")
    public AjaxResult checkCode(@RequestBody Map<String, String> map){
        String code = redisTemplate.opsForValue().get(KEY_PREFIX + map.get("phone"));
        return code != null && code.equals(map.get("code")) ? ajaxResult.ajaxSuccess() : ajaxResult.ajaxFalse();
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/reg")
    public AjaxResult reg(@RequestBody User user){
        return userService.register(user) == 1 ? ajaxResult.ajaxSuccess() : ajaxResult.ajaxFalse();
    }

    /**
     * 判断昵称是否已存在
     * @param user
     * @return
     */
    @PostMapping("/username")
    public AjaxResult username(@RequestBody User user){
        return userService.hasUsername(user) ? ajaxResult.ajaxFalse() : ajaxResult.ajaxSuccess();
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody User user){
        return userService.login(user) == 1 ? ajaxResult.ajaxSuccess() : ajaxResult.ajaxFalse();
    }

}

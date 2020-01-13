package com.learn.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learn.web.pojo.User;
import com.learn.web.service.UserService;
import com.learn.web.util.AjaxResult;
import com.learn.web.util.NumberUtils;
import com.learn.web.util.SmsUtil;
import io.swagger.annotations.ApiKeyAuthDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
            ajaxResult.ajaxSuccess("发送成功,有效时间5分钟");
        }catch (Exception e){
            ajaxResult.ajaxFalse("发送失败,请重试");
        }
        return ajaxResult;
    }

    @PostMapping("/check/code")
    public AjaxResult checkCode(@RequestBody Map<String, String> map){
        String code = redisTemplate.opsForValue().get(KEY_PREFIX + map.get("phone"));
        if(code != null && code.equals(map.get("code")))
            ajaxResult.ajaxSuccess(null);
        else
            ajaxResult.ajaxFalse(null);
        return ajaxResult;
    }

    @PostMapping("/reg")
    public AjaxResult reg(@RequestBody User user){
        if (userService.insertUser(user) == 1) {
            ajaxResult.ajaxSuccess(null);
        } else {
            ajaxResult.ajaxFalse(null);
        }
        return ajaxResult;
    }
}

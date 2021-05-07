package com.yxx.mall.backend.service.impl;

import com.yxx.mall.backend.service.LoginService;
import com.yxx.mall.common.utils.RRException;
import com.yxx.mall.common.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xyong
 * date 2021-05-07
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    RedisService redisService;

    @Override
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        //从redis中取出验证码
        String captcha = redisService.getCacheObject(verifyKey);
        //删除验证码
        redisService.deleteObject(verifyKey);
        //验证验证码
        if(captcha==null){
            throw new RRException("验证码失效，请刷新后重新输入！");
        }
        if(!code.equalsIgnoreCase(captcha)){
            throw new RRException("验证码错误！");
        }
        //用户验证

        return null;
    }
}

package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.model.LoginBody;
import com.yxx.mall.backend.model.LoginUser;
import com.yxx.mall.backend.service.LoginService;
import com.yxx.mall.backend.service.impl.TokenService;
import com.yxx.mall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xyong
 * date 2021-05-07
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/auth/login")
    public R login(@Validated @RequestBody LoginBody loginBody){
        LoginUser loginUser = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        Map<String, Object> token = tokenService.createToken(loginUser);
        return R.ok().put("data",token);
    }
}

package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.model.LoginBody;
import com.yxx.mall.backend.service.LoginService;
import com.yxx.mall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xyong
 * date 2021-05-07
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public R login(@RequestBody LoginBody loginBody){
        String token=loginService.login(loginBody.getUsername(),loginBody.getPassword(),loginBody.getCode(),loginBody.getUuid());
        return R.ok();
    }
}

package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.model.LoginBody;
import com.yxx.mall.backend.model.LoginUser;
import com.yxx.mall.backend.service.LoginService;
import com.yxx.mall.backend.service.impl.TokenService;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xyong
 * date 2021-05-07
 */
@RestController
@RequestMapping("/system")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public R login(@Validated @RequestBody LoginBody loginBody){
        LoginUser loginUser = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        Map<String, Object> token = tokenService.createToken(loginUser);
        return R.ok().put("data",token);
    }

    @DeleteMapping("/logout")
    public R logout(HttpServletRequest request){
        LoginUser loginUser = tokenService.getLoginUser(request);
        if(StringUtils.isNotNull(loginUser)){
            String username = loginUser.getUsername();
            tokenService.delLoginUser(loginUser.getToken());
        }
        return R.ok();
    }
}

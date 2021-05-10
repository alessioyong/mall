package com.yxx.mall.backend.service;

import com.yxx.mall.backend.model.LoginUser;

public interface LoginService {
    LoginUser login(String username, String password, String code, String uuid);
}

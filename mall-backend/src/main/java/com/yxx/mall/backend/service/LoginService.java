package com.yxx.mall.backend.service;

public interface LoginService {
    String login(String username, String password, String code, String uuid);
}

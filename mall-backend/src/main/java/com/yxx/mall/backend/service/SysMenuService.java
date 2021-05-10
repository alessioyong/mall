package com.yxx.mall.backend.service;

import java.util.Set;

public interface SysMenuService {
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);
}

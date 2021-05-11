package com.yxx.mall.backend.service;

import com.yxx.mall.backend.model.RouterVo;
import com.yxx.mall.common.entity.backend.SysMenuEntity;

import java.util.List;
import java.util.Set;

public interface SysMenuService {
    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     * @param userId
     * @return
     */
    List<SysMenuEntity> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需的菜单
     * @param menus
     * @return
     */
    List<RouterVo> buildMenus(List<SysMenuEntity> menus);
}

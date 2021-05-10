package com.yxx.mall.backend.service.impl;

import com.yxx.mall.backend.service.PermissionService;
import com.yxx.mall.backend.service.SysMenuService;
import com.yxx.mall.backend.service.SysRoleService;
import com.yxx.mall.common.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xyong
 * date 2021-05-10
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    @Override
    public Set<String> getRolePermission(Long userId) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(userId)) {
            roles.add("admin");
        }else {
            roles.addAll(roleService.selectRolePermissionByUserId(userId));
        }
        return roles;
    }

    @Override
    public Set<String> getMenuPermission(Long userId) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (SecurityUtils.isAdmin(userId)) {
            perms.add("*:*:*");
        }else {
            perms.addAll(menuService.selectMenuPermsByUserId(userId));
        }
        return perms;
    }
}

package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.service.PermissionService;
import com.yxx.mall.backend.service.SysUserService;
import com.yxx.mall.common.entity.backend.SysUserEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author xyong
 * date 2021-05-10
 */
@RestController
@RequestMapping("/system")
@Slf4j
public class SysUserController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    SysUserService userService;
    @GetMapping("/user/getInfo")
    public R getInfo(){
        Long userId = SecurityUtils.getUserId();
        log.info("用户ID:{}",userId);
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(userId);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(userId);
        SysUserEntity user = userService.getById(userId);
        return R.ok().put("user",user).put("roles",roles).put("permissions",permissions);
    }
}

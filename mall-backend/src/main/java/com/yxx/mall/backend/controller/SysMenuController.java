package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.model.RouterVo;
import com.yxx.mall.backend.service.SysMenuService;
import com.yxx.mall.common.entity.backend.SysMenuEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-11
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Autowired
    SysMenuService menuService;

    @GetMapping("/list")
    public R list(SysMenuEntity menu){
        Long userId = SecurityUtils.getUserId();
        List<SysMenuEntity> menus=menuService.selectMenuList(menu,userId);
        return R.ok().put("data",menus);
    }


    /**
     * 根据用户ID查询路由
     * @return
     */
    @GetMapping("/getRouters")
    public R getRouters(){

        Long userId = SecurityUtils.getUserId();
        List<SysMenuEntity> menus=menuService.selectMenuTreeByUserId(userId);
        List<RouterVo> router=menuService.buildMenus(menus);
        return R.ok().put("data",router);
    }
}

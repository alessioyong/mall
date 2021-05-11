package com.yxx.mall.backend.controller;

import com.yxx.mall.backend.model.RouterVo;
import com.yxx.mall.backend.service.SysMenuService;
import com.yxx.mall.common.entity.backend.SysMenuEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 查询菜单列表
     * @param menu
     * @return
     */
    @GetMapping("/list")
    public R list(SysMenuEntity menu){
        Long userId = SecurityUtils.getUserId();
        List<SysMenuEntity> menus=menuService.selectMenuList(menu,userId);
        return R.ok().put("data",menus);
    }

    @PostMapping("/add")
    public R add(@Validated @RequestBody SysMenuEntity menu){
        int flag=menuService.insertMenu(menu);
        return flag>0?R.ok():R.error();
    }

    /**
     * 根据菜单编号获取详细信息
     */
    //@PreAuthorize(hasPermi = "system:menu:query")
    @GetMapping(value = "/{menuId}")
    public R getInfo(@PathVariable Long menuId)
    {
        SysMenuEntity menu=menuService.selectMenuById(menuId);
        return R.ok().put("data",menu);
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

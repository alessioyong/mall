package com.yxx.mall.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.backend.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuEntity> {

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);
    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenuEntity> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     * @param userId
     * @return
     */
    List<SysMenuEntity> selectMenuTreeByUserId(Long userId);
    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenuEntity> selectMenuList(SysMenuEntity menu);
    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    List<SysMenuEntity> selectMenuListByUserId(SysMenuEntity menu);

    /**
     * 根据菜单编号获取菜单详细信息
     * @param menuId
     * @return
     */
    SysMenuEntity selectMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     * @param menuName
     * @param parentId
     * @return
     */
    SysMenuEntity checkMenuNameUnique(String menuName, Long parentId);

    /**
     * 新增保存菜单信息
     * @param menu
     * @return
     */
    int insertMenu(SysMenuEntity menu);
}

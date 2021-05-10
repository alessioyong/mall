package com.yxx.mall.backend.mapper;

import com.yxx.mall.common.entity.backend.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-10
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 根据用户Id查询角色
     * @param userId
     * @return
     */
    List<SysRoleEntity> selectRolePermissionByUserId(Long userId);
}

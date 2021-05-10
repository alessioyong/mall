package com.yxx.mall.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.backend.mapper.SysUserMapper;
import com.yxx.mall.backend.service.SysUserService;
import com.yxx.mall.common.entity.backend.SysUserEntity;
import org.springframework.stereotype.Service;

/**
 * @author xyong
 * date 2021-05-10
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService{
}

package com.yxx.mall.backend.service.impl;

import com.yxx.mall.backend.mapper.SysMenuMapper;
import com.yxx.mall.backend.service.SysMenuService;
import com.yxx.mall.common.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author xyong
 * date 2021-05-10
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    SysMenuMapper menuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }
}

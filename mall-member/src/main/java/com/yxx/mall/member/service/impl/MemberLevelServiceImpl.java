package com.yxx.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.member.MemberLevelEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.member.mapper.MemberLevelMapper;
import com.yxx.mall.member.service.MemberLevelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-05-26
 */
@Service
public class MemberLevelServiceImpl extends ServiceImpl<MemberLevelMapper, MemberLevelEntity> implements MemberLevelService {

    /**
     * 根据条件分页查询出会员等级
     * @param params
     * @return
     */
    @Override
    public PageInfo listLeves(Map<String, Object> params) {
        Integer pageNum = Integer.valueOf((String) params.get("pageNum"));
        Integer pageSize = Integer.valueOf((String) params.get("pageSize"));
        PageHelper.startPage(pageNum,pageSize);
        String name = (String) params.get("name");
        String defaultStatus = (String) params.get("defaultStatus");
        QueryWrapper<MemberLevelEntity> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        if(StringUtils.isNotEmpty(defaultStatus)){
            Integer integer = Integer.valueOf(defaultStatus);
            wrapper.eq("default_status",integer);
        }
        List<MemberLevelEntity> entities = this.baseMapper.selectList(wrapper);
        PageInfo page=new PageInfo(entities);
        return page;
    }
}

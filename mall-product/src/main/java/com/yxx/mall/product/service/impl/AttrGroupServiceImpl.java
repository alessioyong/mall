package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import com.yxx.mall.product.mapper.AttrGroupMapper;
import com.yxx.mall.product.service.AttrGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyong
 * date 2021-05-17
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroupEntity> implements AttrGroupService {

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Override
    public List<AttrGroupEntity> getAttrGroup(AttrGroupEntity attrGroupEntity) {
        return baseMapper.selectAttrGroupList(attrGroupEntity);
    }
}

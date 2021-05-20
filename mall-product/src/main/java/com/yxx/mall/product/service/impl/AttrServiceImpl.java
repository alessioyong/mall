package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.product.mapper.AttrMapper;
import com.yxx.mall.product.service.AttrService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-20
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, AttrEntity> implements AttrService {

    /**
     * 查询属性，规格参数列表
     * @param attr
     * @return
     */
    @Override
    public List<AttrEntity> selectAttrList(AttrEntity attr) {
        return baseMapper.selectAttrList(attr);
    }
}

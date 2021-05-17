package com.yxx.mall.product.service;

import com.yxx.mall.common.entity.product.AttrGroupEntity;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-17
 */
public interface AttrGroupService {
    List<AttrGroupEntity> getAttrGroup(AttrGroupEntity attrGroupEntity);
}

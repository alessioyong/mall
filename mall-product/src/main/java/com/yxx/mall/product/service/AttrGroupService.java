package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.AttrGroupEntity;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-17
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    List<AttrGroupEntity> getAttrGroup(AttrGroupEntity attrGroupEntity);
}

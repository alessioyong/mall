package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.AttrEntity;

import java.util.List;

public interface AttrService extends IService<AttrEntity> {
    List<AttrEntity> selectAttrList(AttrEntity attr);
}

package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.AttrAttrgroupRelationEntity;
import com.yxx.mall.product.vo.AttrGroupRelationVo;

import java.util.List;

public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {
    void batchSave(List<AttrGroupRelationVo> vos);
}

package com.yxx.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.coupon.SkuFullReductionEntity;
import com.yxx.mall.common.to.SkuReductionTo;

public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {
    void saveSkuReduction(SkuReductionTo reductionTo);
}

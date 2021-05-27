package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.SkuInfoEntity;

public interface SkuInfoService extends IService<SkuInfoEntity> {
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);
}

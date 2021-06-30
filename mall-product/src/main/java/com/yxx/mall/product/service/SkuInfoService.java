package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.SkuInfoEntity;

import java.util.List;
import java.util.Map;

public interface SkuInfoService extends IService<SkuInfoEntity> {
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageInfo getSkuInfoByCondition(Map<String, Object> params);

    List<SkuInfoEntity> getSkusBySpuId(Long spuId);
}

package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.SkuInfoEntity;
import com.yxx.mall.product.mapper.SkuInfoMapper;
import com.yxx.mall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;

/**
 * @author xyong
 * date 2021-05-27
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfoEntity> implements SkuInfoService {
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }
}

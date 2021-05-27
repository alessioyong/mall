package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.SpuInfoDescEntity;

/**
 * @author xyong
 * date 2021-05-27
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {
    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);
}

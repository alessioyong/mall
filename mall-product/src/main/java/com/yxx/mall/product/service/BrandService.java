package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.BrandEntity;

import java.util.List;


public interface BrandService extends IService<BrandEntity> {
    /**
     * 查询品牌列表
     * @param brand
     * @return
     */
    List<BrandEntity> listByBrand(BrandEntity brand);

    /**
     * 批量删除品牌
     * @param brandIds
     */
    void batchRemove(Long[] brandIds);
}

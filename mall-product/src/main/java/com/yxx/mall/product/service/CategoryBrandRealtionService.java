package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.CategoryBrandRelationEntity;

import java.util.List;

public interface CategoryBrandRealtionService extends IService<CategoryBrandRelationEntity> {
    List<CategoryBrandRelationEntity> listByBrandId(Long brandId);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelationEntity);

    void updateBrand(Long brandId, String name);

    void updateCategory(Long catId, String name);
}

package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author xyong
 * @email ${email}
 * @date 2021-05-12 10:31:44
 */
public interface CategoryService extends IService<CategoryEntity> {


    List<CategoryEntity> listWithTree();

    void deleteByIds(List<Long> ids);

    Long[] findCatelogPath(Long catelogId);

    void updateCasecade(CategoryEntity category);

    List<CategoryEntity> getLevelOneCategorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}


package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.CategoryEntity;

import java.util.List;

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
}


package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.ProductAttrValueEntity;

import java.util.List;

/**
 * spu属性值
 *
 * @author xyong
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {


    void saveProductAttr(List<ProductAttrValueEntity> collect);

    List<ProductAttrValueEntity> baseAttrlistforspu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}


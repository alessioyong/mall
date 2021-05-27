package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.ProductAttrValueEntity;
import com.yxx.mall.product.mapper.ProductAttrValueMapper;
import com.yxx.mall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-27
 */
@Service
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueMapper, ProductAttrValueEntity> implements ProductAttrValueService {
    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        this.saveBatch(collect);
    }
}

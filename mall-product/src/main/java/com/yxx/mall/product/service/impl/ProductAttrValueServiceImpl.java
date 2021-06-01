package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.ProductAttrValueEntity;
import com.yxx.mall.product.mapper.ProductAttrValueMapper;
import com.yxx.mall.product.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ProductAttrValueEntity> baseAttrlistforspu(Long spuId) {
        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return entities;
    }

    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        //1、删除这个spuId之前对应的所有属性
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));


        List<ProductAttrValueEntity> collect = entities.stream().map(item -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}

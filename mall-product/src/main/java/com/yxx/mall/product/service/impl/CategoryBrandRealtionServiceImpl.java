package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.BrandEntity;
import com.yxx.mall.common.entity.product.CategoryBrandRelationEntity;
import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.product.mapper.BrandMapper;
import com.yxx.mall.product.mapper.CategoryBrandRealtionMapper;
import com.yxx.mall.product.mapper.CategoryMapper;
import com.yxx.mall.product.service.CategoryBrandRealtionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xyong
 * date 2021-05-19
 */
@Service
public class CategoryBrandRealtionServiceImpl extends ServiceImpl<CategoryBrandRealtionMapper, CategoryBrandRelationEntity> implements CategoryBrandRealtionService {

    @Resource
    CategoryMapper categoryMapper;
    @Resource
    BrandMapper brandMapper;
    /**
     * 根据Id查询关联关系
     * @param brandId
     * @return
     */
    @Override
    public List<CategoryBrandRelationEntity> listByBrandId(Long brandId) {
        List<CategoryBrandRelationEntity> list = this.list(new QueryWrapper<CategoryBrandRelationEntity>()
                .eq("brand_id", brandId));
        return list;
    }

    /**
     * 新增关联关系，并查询相处对应的品牌名称和分类名称
     * @param categoryBrandRelationEntity
     */
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelationEntity) {
        Long brandId = categoryBrandRelationEntity.getBrandId();
        Long catelogId = categoryBrandRelationEntity.getCatelogId();
        CategoryEntity categoryEntity = categoryMapper.selectById(catelogId);
        BrandEntity brandEntity = brandMapper.selectById(brandId);
        categoryBrandRelationEntity.setBrandName(brandEntity.getName());
        categoryBrandRelationEntity.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelationEntity);
    }
}

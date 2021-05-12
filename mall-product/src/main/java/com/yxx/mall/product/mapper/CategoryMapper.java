package com.yxx.mall.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.product.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author xyong
 * @email ${email}
 * @date 2021-05-12 10:31:44
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {
	
}

package com.yxx.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.product.BrandEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-13
 */
@Mapper
public interface BrandMapper extends BaseMapper<BrandEntity> {

    List<BrandEntity> selectBrandList(BrandEntity brand);
}

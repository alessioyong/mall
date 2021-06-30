package com.yxx.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.product.AttrEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-20
 */
@Mapper
public interface AttrMapper extends BaseMapper<AttrEntity> {
    /**
     * 查询商品属性列表
     * @param attr
     * @return
     */
    List<AttrEntity> selectAttrList(AttrEntity attr);

    List<Long> selectSearchAttrIds(@Param("attrIds") List<Long> attrIds);
}

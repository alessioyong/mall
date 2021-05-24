package com.yxx.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.product.AttrAttrgroupRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttrAttrgroupRelationMapper extends BaseMapper<AttrAttrgroupRelationEntity> {
    void deleteBathRelation(@Param("entities") List<AttrAttrgroupRelationEntity> entities);
}

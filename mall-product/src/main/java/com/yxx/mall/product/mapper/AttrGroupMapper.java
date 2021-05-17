package com.yxx.mall.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AttrGroupMapper extends BaseMapper<AttrGroupEntity> {

    List<AttrGroupEntity> selectAttrGroupList(AttrGroupEntity attrGroupEntity);
}

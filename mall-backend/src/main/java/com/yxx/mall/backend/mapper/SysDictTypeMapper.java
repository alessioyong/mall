package com.yxx.mall.backend.mapper;

import com.yxx.mall.common.entity.backend.SysDictDataEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysDictTypeMapper {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataEntity> selectDictDataByType(String dictType);
}

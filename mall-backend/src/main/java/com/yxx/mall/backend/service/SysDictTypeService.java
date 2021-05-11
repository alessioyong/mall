package com.yxx.mall.backend.service;

import com.yxx.mall.common.entity.backend.SysDictDataEntity;

import java.util.List;

public interface SysDictTypeService {
    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    List<SysDictDataEntity> selectDictDataByType(String dictType);
}

package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.SpuInfoEntity;
import com.yxx.mall.product.vo.SpuSaveVo;

import java.util.Map;

public interface SpuInfoService extends IService<SpuInfoEntity> {
    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageInfo getSpuInfoByCondition(Map<String, Object> params);

    /**
     * 商品上架
     * @param id
     */
    void up(Long id);
}

package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.SpuInfoEntity;
import com.yxx.mall.product.vo.SpuSaveVo;

public interface SpuInfoService extends IService<SpuInfoEntity> {
    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);
}

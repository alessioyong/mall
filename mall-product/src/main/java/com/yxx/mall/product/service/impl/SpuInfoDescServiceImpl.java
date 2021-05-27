package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.SpuInfoDescEntity;
import com.yxx.mall.product.mapper.SpuInfoDescMapper;
import com.yxx.mall.product.service.SpuInfoDescService;
import org.springframework.stereotype.Service;

/**
 * @author xyong
 * date 2021-05-27
 */
@Service
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescMapper, SpuInfoDescEntity> implements SpuInfoDescService {
    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity descEntity) {
        this.baseMapper.insert(descEntity);
    }
}

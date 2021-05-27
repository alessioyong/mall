package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yxx.mall.common.entity.product.SpuImagesEntity;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-27
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {
    void saveImages(Long id, List<String> images);
}

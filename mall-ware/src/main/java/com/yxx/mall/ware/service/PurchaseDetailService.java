package com.yxx.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.PurchaseDetailEntity;

import java.util.Map;

/**
 * @author xyong
 * date 2021-06-24
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {
    PageInfo listByConditions(Map<String, Object> parmas);
}
